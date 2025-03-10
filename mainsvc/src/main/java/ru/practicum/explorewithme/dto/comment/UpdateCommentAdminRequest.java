package ru.practicum.explorewithme.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.model.Comment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentAdminRequest {
    @NotNull
    private Comment.CommentStatus status;
}