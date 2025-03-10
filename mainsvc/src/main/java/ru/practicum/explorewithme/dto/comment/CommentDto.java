package ru.practicum.explorewithme.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.model.Comment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private Long eventId;
    private Long authorId;
    private String text;
    private Comment.CommentStatus status;
    private LocalDateTime created;
    private LocalDateTime updated;
}
