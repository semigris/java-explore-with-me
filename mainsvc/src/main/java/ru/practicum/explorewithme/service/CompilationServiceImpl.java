package ru.practicum.explorewithme.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.campilation.CompilationDto;
import ru.practicum.explorewithme.dto.campilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.campilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.service.base.CompilationService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private CompilationMapper compilationMapper;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.debug("Добавление подборки: {}", newCompilationDto);
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);

        Compilation savedCompilation = compilationRepository.save(compilation);
        log.debug("Подборка добавлена : {}", savedCompilation);
        return compilationMapper.toCompilationDto(savedCompilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.debug("Добавление подборки с id: {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));

        compilationRepository.delete(compilation);
        log.debug("Подборка удалена");
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        log.debug("Обновление подборки с id: {}. Новые данные: {}", compId, updateCompilationRequest);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));

        compilation = compilationMapper.toCompilation(compilation, updateCompilationRequest);
        Compilation updatedCompilation = compilationRepository.save(compilation);

        log.debug("Подборка обновлена: {}", updatedCompilation);
        return compilationMapper.toCompilationDto(updatedCompilation);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        log.debug("Получение подборки по id: {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));

        log.debug("Подборка найдена: {}", compilation);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.debug("Получение подборок с параметрами: pinned={}, from={}, size={}", pinned, from, size);

        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, pageRequest);

        log.debug("Подборки с параметрами найдены: {}", compilations);
        return compilations.stream()
                .map(compilationMapper::toCompilationDto)
                .toList();
    }
}
