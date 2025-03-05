package ru.practicum.explorewithme.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.CategoryRepository;

import java.time.LocalDateTime;


@Component
@AllArgsConstructor
public class EventMapper {
    private CategoryMapper categoryMapper;
    private UserMapper userMapper;
    private CategoryRepository categoryRepository;

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public Event toEvent(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Категория с id " + updateEventAdminRequest.getCategory() + " не найдена"));
            event.setCategory(category);
        }

        if (updateEventAdminRequest.getAnnotation() != null)
            event.setAnnotation(updateEventAdminRequest.getAnnotation());

        if (updateEventAdminRequest.getDescription() != null)
            event.setDescription(updateEventAdminRequest.getDescription());

        if (updateEventAdminRequest.getEventDate() != null)
            event.setEventDate(updateEventAdminRequest.getEventDate());

        if (updateEventAdminRequest.getLocation() != null)
            event.setLocation(updateEventAdminRequest.getLocation());

        if (updateEventAdminRequest.getPaid() != null)
            event.setPaid(updateEventAdminRequest.getPaid());

        if (updateEventAdminRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());

        if (updateEventAdminRequest.getRequestModeration() != null)
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());

        if (updateEventAdminRequest.getTitle() != null)
            event.setTitle(updateEventAdminRequest.getTitle());

        event.setPublishedOn(LocalDateTime.now());

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(UpdateEventAdminRequest.AdminStateAction.PUBLISH_EVENT)) {
                event.setState(Event.EventState.PUBLISHED);
            } else if (updateEventAdminRequest.getStateAction().equals(UpdateEventAdminRequest.AdminStateAction.REJECT_EVENT)) {
                event.setState(Event.EventState.CANCELED);
            }
        }
        return event;
    }

    public Event toEvent(Event event, UpdateEventUserRequest updateEventUserRequest) {
        if (updateEventUserRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Категория с id " + updateEventUserRequest.getCategory() + " не найдена"));
            event.setCategory(category);
        }

        if (updateEventUserRequest.getAnnotation() != null)
            event.setAnnotation(updateEventUserRequest.getAnnotation());

        if (updateEventUserRequest.getDescription() != null)
            event.setDescription(updateEventUserRequest.getDescription());

        if (updateEventUserRequest.getEventDate() != null)
            event.setEventDate(updateEventUserRequest.getEventDate());

        if (updateEventUserRequest.getLocation() != null)
            event.setLocation(updateEventUserRequest.getLocation());

        if (updateEventUserRequest.getPaid() != null)
            event.setPaid(updateEventUserRequest.getPaid());

        if (updateEventUserRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());

        if (updateEventUserRequest.getRequestModeration() != null)
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());

        if (updateEventUserRequest.getTitle() != null)
            event.setTitle(updateEventUserRequest.getTitle());

        event.setPublishedOn(LocalDateTime.now());

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(UpdateEventUserRequest.UserStateAction.SEND_TO_REVIEW)) {
                event.setState(Event.EventState.PENDING);
            } else if (updateEventUserRequest.getStateAction().equals(UpdateEventUserRequest.UserStateAction.CANCEL_REVIEW)) {
                event.setState(Event.EventState.CANCELED);
            }
        }
        return event;
    }

    public Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(initiator)
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(Event.EventState.PENDING)
                .title(newEventDto.getTitle())
                .createdOn(LocalDateTime.now())
                .build();
    }
}

