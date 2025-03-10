package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.base.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.model.Comment.CommentStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;


    @Override
    public CommentDto getComment(Long commentId, Long userId) {
        log.debug("Получение комментария с id: {} пользователем: {}", commentId, userId);
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " от пользователя с id:" + userId + " не найден"));

        log.debug("Комментарий найден: {}", comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getCommentsByStatus(Long eventId, String status) {
        log.debug("Получение комментариев с параметрами: eventId={}, status={}", eventId, status);

        Comment.CommentStatus commentStatus = null;
        if (status != null) {
            try {
                commentStatus = Comment.CommentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Некорректный статус комментария: " + status);
            }
        }

        List<Comment> comments;
        if (eventId == null) {
            comments = commentRepository.findByStatus(commentStatus);
        } else {
            eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));
            comments = commentRepository.findByEventIdAndStatus(eventId, commentStatus);
        }

        log.debug("Найдены комментарии со статусом {}: {}", status, comments);
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    public List<CommentDto> getCommentsByEvent(Long eventId, Long userId) {
        log.debug("Получение списка комментариев к событию с id: {} пользователем: {}", eventId, userId);

        List<Comment> comments = commentRepository.findByEventIdAndAuthorId(eventId, userId);

        if (comments.isEmpty())
            throw new NotFoundException("Комментарии от пользователя с id: " + userId + " не найдены");

        log.debug("Получен список комментариев: {}", comments);
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto createComment(NewCommentDto newCommentDto, Long eventId, Long userId) {
        log.debug("Создание нового комментария: {} пользователем с id: {} к событию c id: {}", newCommentDto, userId, eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id: " + eventId + " не найдено"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));

        Comment comment = commentMapper.toComment(newCommentDto, event, author);
        Comment savedComment = commentRepository.save(comment);

        log.debug("Создан новый комментарий: {}", savedComment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long commentId, String newText, Long userId) {
        log.debug("Обновление комментария с id: {} пользователем с id: {}", commentId, userId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не найден"));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Изменить можно только свой комментарий");
        }
        comment.setText(newText);
        comment.setUpdated(LocalDateTime.now());
        comment.setStatus(PENDING);
        Comment updatedComment = commentRepository.save(comment);

        log.debug("Комментарий обновлен: {}", updatedComment);
        return commentMapper.toCommentDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        log.debug("Удаление  комментария с id: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не найден"));

        commentRepository.delete(comment);
        log.debug("Комментарий с id: {} удален", commentId);
    }

    @Override
    @Transactional
    public CommentDto updateCommentStatus(Long commentId, Comment.CommentStatus status) {
        log.debug("Изменение статуса комментария с id: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий с id: " + commentId + " не найден"));

        if (comment.getStatus() == PUBLISHED && status == PUBLISHED) {
            throw new ConflictException("Комментарий уже одобрен");
        }

        if (comment.getStatus() == CANCELED && status == CANCELED) {
            throw new ConflictException("Комментарий уже отклонен");
        }

        comment.setStatus(status);
        comment.setUpdated(LocalDateTime.now());

        log.debug("Статус комментария с id: {} изменен на {}", commentId, status);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}
