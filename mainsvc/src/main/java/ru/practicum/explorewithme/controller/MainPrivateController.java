package ru.practicum.explorewithme.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentUserRequest;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.base.CommentService;
import ru.practicum.explorewithme.service.base.EventService;
import ru.practicum.explorewithme.service.base.RequestService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}")
public class MainPrivateController {

    private final EventService eventService;
    private final RequestService requestService;
    private final CommentService commentService;

    /**
     * Private: События
     * <p>
     * GET /users/{userId}/events Получение событий, добавленных текущим пользователем
     */

    @GetMapping("/events")
    public List<EventShortDto> getEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.debug("Запрос на получение событий пользователя с id: {}, параметры: from={}, size={}", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    /**
     * POST /users/{userId}/events Добавление нового события.
     */

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(
            @PathVariable Long userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) {
        log.debug("Запрос на добавление нового события для пользователя с id: {}, данные: {}", userId, newEventDto);
        return eventService.addEvent(userId, newEventDto);
    }

    /**
     * GET /users/{userId}/events/{eventId} Получение полной информации о событии добавленном текущим пользователем
     */
    @GetMapping("/events/{eventId}")
    public EventFullDto getEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.debug("Запрос на получение полной информации о событии с id: {} для пользователя с id: {}", eventId, userId);
        return eventService.getEvent(userId, eventId);
    }

    /**
     * PATCH /users/{userId}/events/{eventId} Изменение события добавленного текущим пользователем.
     */
    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest
    ) {
        log.debug("Запрос на обновление события с id: {} для пользователя с id: {}. Новые данные: {}", eventId, userId, updateEventUserRequest);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    /**
     * GET /users/{userId}/events/{eventId}/requests Получение информации о запросах на участие в событии текущего пользователя.
     */

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        log.debug("Запрос на получение информации о запросах на участие в событии с id: {} для пользователя с id: {}", eventId, userId);
        return eventService.getEventParticipants(userId, eventId);
    }

    /**
     * PATCH /users/{userId}/events/{eventId}/requests Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя.
     */

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest requestStatusUpdate) {
        log.debug("Запрос на изменение статуса заявок на участие в событии с id: {} для пользователя с id: {}, данные: {}", eventId, userId, requestStatusUpdate);
        return eventService.changeRequestStatus(userId, eventId, requestStatusUpdate);
    }

    /**
     * Private: Запросы на участие
     * <p>
     * GET /users/{userId}/requests Получение информации о заявках текущего пользователя на участие в чужих событиях.
     */

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId) {
        log.debug("Запрос на получение информации о заявках текущего пользователя с id: {} на участие в чужих событиях", userId);
        return requestService.getUserRequests(userId);
    }

    /**
     * POST /users/{userId}/requests Добавление запроса от текущего пользователя на участие в событии.
     */

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable Long userId,
                                                           @RequestParam(required = false) Long eventId) {
        log.debug("Запрос на добавление запроса от пользователя с id: {} на участие в событии с id: {}", userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    /**
     * PATCH /users/{userId}/requests/{requestId}/cancel Отмена своего запроса на участие в событии.
     */

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.debug("Запрос на отмену запроса на участие с id: {} для пользователя с id: {}", requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }

    /**
     * Private: Комментариии
     * <p>
     * POST /users/{userId}/comments/{eventId} Создание нового комментария к событию текущим пользователем.
     */

    @PostMapping("/comments/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long eventId,
                                    @PathVariable Long userId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.debug("Запрос на создание комментария к событию с id: {} пользователем с id: {}", eventId, userId);
        return commentService.createComment(newCommentDto, eventId, userId);
    }

    /**
     * PATCH /users/{userId}/comments/{commentId} Обновление комментария текущего пользователя.
     * Обновлять можно как опубликованные, так и отмененные модерацией комментарии.
     * После обновления комментарий автоматически меняет статус и снова отправляется на модерацию.
     */
    @PatchMapping("/comments/{commentId}")
    public CommentDto updateOwnComment(@PathVariable Long commentId,
                                       @PathVariable Long userId,
                                       @RequestBody @Valid UpdateCommentUserRequest updateCommentUserRequest) {
        log.debug("Запрос на обновление своего комментария с id: {} пользователем с id: {}", commentId, userId);
        return commentService.updateComment(commentId, updateCommentUserRequest.getText(), userId);
    }

    /**
     * GET /users/{userId}/comments/{commentId} Получение информации о конкретном комментарии текущего пользователя.
     */
    @GetMapping("/comments/{commentId}")
    public CommentDto getComment(@PathVariable Long commentId,
                                 @PathVariable Long userId) {
        log.debug("Запрос на получение комментария с id: {} пользователем: {}", commentId, userId);
        return commentService.getComment(commentId, userId);
    }

    /**
     * GET /users/{userId}/comments/events/{eventId} Получение всех комментариев к событию текущим пользователем.
     */
    @GetMapping("/comments/events/{eventId}")
    public List<CommentDto> getCommentsByEvent(@PathVariable Long eventId,
                                               @PathVariable Long userId) {
        log.debug("Запрос на получение всех комментариев к событию с id: {} пользователем: {}", eventId, userId);
        return commentService.getCommentsByEvent(eventId, userId);
    }
}
