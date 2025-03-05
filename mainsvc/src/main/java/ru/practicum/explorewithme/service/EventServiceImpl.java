package ru.practicum.explorewithme.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.service.base.CategoryService;
import ru.practicum.explorewithme.service.base.EventService;
import ru.practicum.explorewithme.service.base.RequestService;
import ru.practicum.explorewithme.service.base.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.dto.event.UpdateEventAdminRequest.AdminStateAction.PUBLISH_EVENT;
import static ru.practicum.explorewithme.dto.event.UpdateEventAdminRequest.AdminStateAction.REJECT_EVENT;
import static ru.practicum.explorewithme.model.Event.EventState.*;

@Slf4j
@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestService requestService;
    private final StatsClient statsClient;

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(Long userId, Long eventId) {
        log.debug("Получение полной информации о событии с id: {} для пользователя с id: {}", eventId, userId);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + "для пользователя с id: " + userId + " не найдено"));

        Map<Long, Long> viewStatsMap = getViews(List.of(event));
        Long views = viewStatsMap.getOrDefault(event.getId(), 0L);
        event.setViews(views);

        log.debug("Информация о событии найдена: {}", views);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(Long id, HttpServletRequest request) {
        log.debug("Получение подробной информации о событии с id: {}", id);
        statsClient.saveHit(createHit(request));

        Event event = eventRepository.findById(id).filter(e -> e.getState().equals(PUBLISHED))
                .orElseThrow(() -> new NotFoundException("Событие с id:" + id + " не найдено"));

        Map<Long, Long> viewStatsMap = getViews(List.of(event));
        Long views = viewStatsMap.getOrDefault(event.getId(), 0L);
        event.setViews(views);

        log.debug("Информация о событии найдена: {}", views);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.debug("Получение событий с параметрами: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("eventDate").descending());

        List<Event> events;
        if (rangeStart == null || rangeEnd == null) {
            events = eventRepository.findEventsWithoutDate(users, states, categories, pageable);
        } else {
            events = eventRepository.findEvents(users, states, categories, rangeStart, rangeEnd, pageable);
        }

        Map<Long, Long> viewStatsMap = getViews(events);
        for (Event event : events) {
            Long viewsFromMap = viewStatsMap.getOrDefault(event.getId(), 0L);
            event.setViews(viewsFromMap);
        }

        log.debug("События с параметрами получены: {}", events);
        return events.stream().map(eventMapper::toEventFullDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) {
        log.debug("Получение событий с параметрами: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        statsClient.saveHit(createHit(request));

        if (rangeEnd != null && rangeStart.isAfter(rangeEnd))
            throw new BadRequestException("Дата начала события не может быть позже даты окончания");

        text = (text != null) ? text.toLowerCase() : null;

        categories = (categories == null || categories.isEmpty()) ? Collections.emptyList() : categories;

        Pageable pageable = null;
        if (sort == null) {
            pageable = PageRequest.of(from / size, size);
        } else if ("EVENT_DATE".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
        } else if ("VIEWS".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(from / size, size, Sort.by("views").ascending());
        }

        List<Event> events;
        if (rangeStart != null && rangeEnd != null) {
            events = eventRepository.findPublishedEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);
        } else {
            events = eventRepository.findPublishedEventsWithoutDate(text, categories, paid, LocalDateTime.now(), onlyAvailable, pageable);
        }

        Map<Long, Long> viewStatsMap = getViews(events);
        for (Event event : events) {
            Long viewsFromMap = viewStatsMap.getOrDefault(event.getId(), 0L);
            event.setViews(viewsFromMap);
        }

        log.debug("События с параметрами получены: {}", events);
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        log.debug("Получение событий пользователя с id: {}, параметры: from={}, size={}", userId, from, size);
        userService.getUserById(userId);

        List<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size));

        Map<Long, Long> viewStatsMap = getViews(events);
        for (Event event : events) {
            Long viewsFromMap = viewStatsMap.getOrDefault(event.getId(), 0L);
            event.setViews(viewsFromMap);
        }

        log.debug("События пользователя найдены: {}", events);
        return events.stream().map(eventMapper::toEventShortDto).toList();
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        log.debug("Добавление нового события для пользователя с id: {}, данные: {}", userId, newEventDto);

        User user = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(newEventDto.getCategory());

        Event event = eventMapper.toEvent(newEventDto, user, category);
        Event savedEvent = eventRepository.save(event);

        log.debug("Новое событие добавлено: {}", savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("Обновление события с id: {}. Новые данные: {}", eventId, updateEventAdminRequest);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие на найдено."));

        if (updateEventAdminRequest.getStateAction() != null && event.getState() != null) {
            if (updateEventAdminRequest.getStateAction().equals(PUBLISH_EVENT) && event.getState().equals(PUBLISHED)) {
                throw new ConflictException("Нельзя опубликовать уже опубликованное событие");
            }
            if (updateEventAdminRequest.getStateAction().equals(PUBLISH_EVENT) && event.getState().equals(CANCELED)) {
                throw new ConflictException("Нельзя опубликовать отмененное событие");
            }
            if (updateEventAdminRequest.getStateAction().equals(REJECT_EVENT) && event.getState().equals(PUBLISHED)) {
                throw new ConflictException("Нельзя отменить опубликованное событие");
            }
        }

        eventMapper.toEvent(event, updateEventAdminRequest);
        Event updatedEvent = eventRepository.save(event);

        log.debug("Событие обновлено {}", updatedEvent);
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Обновление события с id: {} для пользователя с id: {}. Новые данные: {}", eventId, userId, updateEventUserRequest);
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + "для пользователя с id: " + userId + " не найдено"));

        if (!event.getState().equals(PENDING) && !event.getState().equals(CANCELED)) {
            throw new ConflictException("Изменить можно только отмененные события или события на модерации");
        }
        if (updateEventUserRequest.getEventDate() != null) {
            LocalDateTime eventDate = updateEventUserRequest.getEventDate();
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Дата события не может быть раньше, чем через два часа от текущего момента");
            }
        }

        Event eventForUpdate = eventMapper.toEvent(event, updateEventUserRequest);

        log.debug("Обновлено событие {} ", eventForUpdate);
        return eventMapper.toEventFullDto(eventRepository.save(eventForUpdate));
    }


    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        log.debug("Получение информации о запросах на участие в событии с id: {} для пользователя с id: {}", eventId, userId);
        eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + "для пользователя с id: " + userId + " не найдено"));
        return requestService.getRequestsByEventId(eventId);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest requestStatusUpdate) {
        log.debug("Изменение статуса заявок на участие в событии с id: {} для пользователя с id: {}, данные: {}", eventId, userId, requestStatusUpdate);
        eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + "для пользователя с id: " + userId + " не найдено"));
        return requestService.updateRequestStatus(eventId, requestStatusUpdate);
    }

    private EndpointHitDto createHit(HttpServletRequest request) {
        return EndpointHitDto.builder()
                .app("mainsvc")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Map<Long, Long> getViews(List<Event> events) {
        LocalDateTime dateForStart = events.stream()
                .map(Event::getCreatedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .toList();

        if (dateForStart == null || uris.isEmpty()) {
            return Collections.emptyMap();
        }

        ResponseEntity<Object> response = statsClient.getStats(dateForStart, LocalDateTime.now(), uris, true);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStatsDto> viewStatsList = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });

        return viewStatsList.stream()
                .filter(statsDto -> statsDto.getUri().startsWith("/events/"))
                .collect(Collectors.toMap(
                        statsDto -> Long.parseLong(statsDto.getUri().substring("/events/".length())),
                        ViewStatsDto::getHits
                ));
    }
}


