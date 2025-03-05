package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.ParticipationRequest;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.base.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.debug("Получение всех запросов пользователя с id: {} на участие в событиях", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id:" + userId + " не найден"));

        List<ParticipationRequest> requests = requestRepository.findByRequesterId(userId);

        log.debug("Запросы пользователя на участие в событиях: {}", requests);
        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        log.debug("Добавление запроса на участие в событии с id: {} для пользователя с id: {}", eventId, userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id:" + eventId + " не найдено"));
        log.debug("Событие для участия: {}", event);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id:" + userId + " не найден"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя добавить запрос на участие от инициатора события");
        }

        if (!event.getState().equals(Event.EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).isPresent()) {
            throw new ConflictException("Запрос на это событие этим пользователем уже существует");
        }

        Long approvedRequests = Objects.requireNonNullElse(event.getConfirmedRequests(), 0L);
        if (event.getParticipantLimit() > 0 && approvedRequests >= event.getParticipantLimit()) {
            throw new ConflictException("Лимит участников события достигнут");
        }

        ParticipationRequest request = new ParticipationRequest();
        request.setEvent(event);
        request.setRequester(user);
        if (event.getParticipantLimit() == 0 || Boolean.TRUE.equals(!event.getRequestModeration())) {
            request.setStatus(ParticipationRequest.RequestStatus.CONFIRMED);
            event.setConfirmedRequests(approvedRequests + 1);
        } else {
            request.setStatus(ParticipationRequest.RequestStatus.PENDING);
        }
        request.setCreated(LocalDateTime.now());

        ParticipationRequest savedRequest = requestRepository.save(request);

        log.debug("Запрос на участие в событии добавлен {}", savedRequest);
        return requestMapper.toParticipationRequestDto(savedRequest);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.debug("Отмена запроса на участие с id: {} для пользователя с id: {}", requestId, userId);

        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id: " + requestId + " не найден"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException("Отменить можно только свой запрос");
        }

        request.setStatus(ParticipationRequest.RequestStatus.CANCELED);

        log.debug("Запрос на участие отменен");
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByEventId(Long eventId) {
        log.debug("Получение всех запросов на участие в событии с id: {}", eventId);
        List<ParticipationRequest> requests = requestRepository.findByEventId(eventId);
        return requests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long eventId, EventRequestStatusUpdateRequest requestStatusUpdate) {
        log.debug("Обновление статуса запросов для события с id: {}, данные: {}", eventId, requestStatusUpdate);

        List<Long> requestIds = requestStatusUpdate.getRequestIds();
        String status = requestStatusUpdate.getStatus();

        List<ParticipationRequest> requests = requestRepository.findAllById(requestIds);

        if (requests.isEmpty()) {
            throw new NotFoundException("Запросы с этими ids отсутвуют");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        Long approvedRequests = Objects.requireNonNullElse(event.getConfirmedRequests(), 0L);

        Long participantLimit = event.getParticipantLimit();
        long availableSlots = participantLimit - approvedRequests;

        if (participantLimit > 0 && approvedRequests >= participantLimit) {
            throw new ConflictException("Лимит участников события достигнут");
        }

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        if (status.equals("CONFIRMED")) {
            for (ParticipationRequest request : requests) {
                if (!request.getEvent().getId().equals(eventId)) continue;
                if (request.getStatus() != ParticipationRequest.RequestStatus.PENDING) {
                    throw new ConflictException("Запрос с id: " + request.getId() + " уже отклонен.");
                }
                if (confirmedRequests.size() < availableSlots) {
                    request.setStatus(ParticipationRequest.RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                } else {
                    request.setStatus(ParticipationRequest.RequestStatus.REJECTED);
                    rejectedRequests.add(request);
                }
            }
            event.setConfirmedRequests(approvedRequests + confirmedRequests.size());
        } else if (status.equals("REJECTED")) {
            for (ParticipationRequest request : requests) {
                if (!request.getEvent().getId().equals(eventId)) continue;
                if (request.getStatus() == ParticipationRequest.RequestStatus.CONFIRMED) {
                    throw new ConflictException("Нельзя отклонить подтвержденный запрос");
                }
                request.setStatus(ParticipationRequest.RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        } else {
            throw new ConflictException("Некорректный стутус : " + status);
        }

        requestRepository.saveAll(requests);
        eventRepository.save(event);

        log.debug("Статусы запросов обновлены: {}", event);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests.stream()
                        .map(requestMapper::toParticipationRequestDto)
                        .toList())
                .rejectedRequests(rejectedRequests.stream()
                        .map(requestMapper::toParticipationRequestDto)
                        .toList())
                .build();
    }
}
