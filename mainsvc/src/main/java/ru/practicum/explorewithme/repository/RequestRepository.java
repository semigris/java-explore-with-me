package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findByEventId(Long eventId);

    List<ParticipationRequest> findByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByEventIdAndRequesterId(Long eventId, Long requesterId);
}
