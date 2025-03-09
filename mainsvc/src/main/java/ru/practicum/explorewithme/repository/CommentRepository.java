package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventIdAndStatus(Long eventId, Comment.CommentStatus status);

    Optional<List<Comment>> findByEventIdAndAuthorId(Long eventId, Long authorId);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long authorId);

    List<Comment> findByStatus(Comment.CommentStatus commentStatus);
}
