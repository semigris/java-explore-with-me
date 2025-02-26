package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("""
            SELECT new ru.practicum.explorewithme.model.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip))
            FROM EndpointHit AS h
            WHERE h.timestamp BETWEEN :start AND :end
            AND ((h.uri IN (:uris)) OR (:uris IS NULL))
            GROUP BY h.app, h.uri
            ORDER BY COUNT(DISTINCT h.ip) DESC
            """)
    List<ViewStats> findUniqueStat(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
            SELECT new ru.practicum.explorewithme.model.ViewStats(h.app, h.uri, COUNT(h.ip))
            FROM EndpointHit AS h
            WHERE h.timestamp BETWEEN :start AND :end
            AND ((h.uri IN (:uris)) OR (:uris IS NULL))
            GROUP BY h.app, h.uri
            ORDER BY COUNT(h.ip) DESC
            """)
    List<ViewStats> findStat(LocalDateTime start, LocalDateTime end, List<String> uris);
}
