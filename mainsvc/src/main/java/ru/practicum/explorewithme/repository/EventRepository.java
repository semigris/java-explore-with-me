package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategoryId(Long categoryId);

    List<Event> findByInitiatorId(Long userId, PageRequest pageRequest);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("""
            SELECT e FROM Event e
            JOIN FETCH e.category AS c
            JOIN FETCH e.initiator AS i
            JOIN FETCH e.location AS l
            WHERE (:users IS NULL OR e.initiator.id IN :users)
            AND (:states IS NULL OR e.state IN :states)
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd)
            ORDER BY e.views DESC
            """)
    List<Event> findEvents(@Param("users") List<Long> users,
                           @Param("states") List<String> states,
                           @Param("categories") List<Long> categories,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           Pageable pageable);

    @Query("""
            SELECT e FROM Event e
            JOIN FETCH e.category AS c
            JOIN FETCH e.initiator AS i
            JOIN FETCH e.location AS l
            WHERE (:users IS NULL OR e.initiator.id IN :users)
            AND (:states IS NULL OR e.state IN :states)
            AND (:categories IS NULL OR e.category.id IN :categories)
            ORDER BY e.views DESC
            """)
    List<Event> findEventsWithoutDate(@Param("users") List<Long> users,
                                      @Param("states") List<String> states,
                                      @Param("categories") List<Long> categories,
                                      Pageable pageable);

    @Query("""
            SELECT e FROM Event e
            JOIN FETCH e.category AS c
            JOIN FETCH e.initiator AS i
            JOIN FETCH e.location AS l
            WHERE (:text IS NULL OR (e.annotation ILIKE %:text% OR e.description ILIKE %:text%))
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (:paid IS NULL OR e.paid = :paid)
            AND (:onlyAvailable = FALSE OR e.participantLimit > e.confirmedRequests)
            AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd)
            AND e.state = PUBLISHED
            """)
    List<Event> findPublishedEvents(@Param("text") String text,
                                    @Param("categories") List<Long> categories,
                                    @Param("paid") Boolean paid,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    @Param("onlyAvailable") Boolean onlyAvailable,
                                    Pageable pageable);

    @Query("""
            SELECT e FROM Event e
            JOIN FETCH e.category AS c
            JOIN FETCH e.initiator AS i
            JOIN FETCH e.location AS l
            WHERE (:text IS NULL OR (e.annotation ILIKE %:text% OR e.description ILIKE %:text%))
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (:paid IS NULL OR e.paid = :paid)
            AND (:onlyAvailable = FALSE OR e.participantLimit > e.confirmedRequests)
            AND e.eventDate > :now
            AND e.state = PUBLISHED
            """)
    List<Event> findPublishedEventsWithoutDate(@Param("text") String text,
                                               @Param("categories") List<Long> categories,
                                               @Param("paid") Boolean paid,
                                               @Param("now") LocalDateTime now,
                                               @Param("onlyAvailable") Boolean onlyAvailable,
                                               Pageable pageable);
}

