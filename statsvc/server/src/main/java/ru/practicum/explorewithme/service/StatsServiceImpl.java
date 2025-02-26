package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.EndpointHitDto;
import ru.practicum.explorewithme.ViewStatsDto;
import ru.practicum.explorewithme.exception.UnavailableDataException;
import ru.practicum.explorewithme.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.mapper.ViewStatsMapper;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final ViewStatsMapper viewStatsMapper;
    private final EndpointHitMapper endpointHitMapper;

    /**
     * Добавление записи о запросе к эндпоинту
     */
    @Override
    @Transactional
    public EndpointHitDto saveHit(EndpointHitDto endpointHit) {
        log.debug("Обработка добавления записи о запросе к эндпоинту: {}", endpointHit);

        EndpointHit hit = endpointHitMapper.toEndpointHit(endpointHit);
        statsRepository.save(hit);
        EndpointHitDto savedHit = endpointHitMapper.toEndpointHitDto(hit);

        log.debug("Запись о запросе к эндпоинту добавлена: {}", savedHit);
        return savedHit;
    }

    /**
     * Получение статистики по посещениям
     */
    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.debug("Обработка запроса на получение статистики по посещениям за период с: {} до: {}. " +
                  "По uris: {}. Только с уникальным ip: {}", start, end, uris, unique);

        if (start == null || end == null) {
            throw new UnavailableDataException("Параметры start и end не могут быть null");
        }

        List<ViewStats> stats;
        if (unique) {
            stats = statsRepository.findUniqueStat(start, end, uris);
        } else {
            stats = statsRepository.findStat(start, end, uris);
        }

        log.debug("Статистики по посещениям найдена: {}", stats);
        return stats.stream().map(viewStatsMapper::toViewStatsDto).toList();
    }

}
