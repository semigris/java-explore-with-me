package ru.practicum.explorewithme.service.base;

import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.model.Comment;

import java.util.List;

public interface CommentService {
    CommentDto getComment(Long commentId, Long userId);

    List<CommentDto> getCommentsByStatus(Long eventId, String status);

    CommentDto createComment(NewCommentDto dto, Long eventId, Long userId);

    CommentDto updateComment(Long commentId, String newText, Long userId);

    void deleteComment(Long commentId);

    List<CommentDto> getCommentsByEvent(Long eventId, Long userId);

    CommentDto updateCommentStatus(Long commentId, Comment.CommentStatus status);
}
