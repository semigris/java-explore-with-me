package ru.practicum.explorewithme.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .text(comment.getText())
                .status(comment.getStatus())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }

    public Comment toComment(NewCommentDto newCommentDto, Event event, User author) {
        return Comment.builder()
                .event(event)
                .author(author)
                .text(newCommentDto.getText())
                .status(Comment.CommentStatus.PENDING)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }
}
