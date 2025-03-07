package ru.practicum.explorewithme.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.campilation.CompilationDto;
import ru.practicum.explorewithme.dto.campilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.campilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.repository.EventRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
@AllArgsConstructor
public class CompilationMapper {
    EventRepository eventRepository;
    EventMapper eventMapper;

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> events = null;
        if (compilation.getEvents() != null) {
            events = compilation.getEvents().stream().map(eventMapper::toEventShortDto).toList();
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = Collections.emptyList();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
            if (events.size() != newCompilationDto.getEvents().size()) {
                throw new NotFoundException("Одно или несколько событий не найдены");
            }
        }

        return Compilation.builder()
                .events(new HashSet<>(events))
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public Compilation toCompilation(Compilation compilation, UpdateCompilationRequest updateCompilationRequest) {
        if (updateCompilationRequest.getPinned() != null)
            compilation.setPinned(updateCompilationRequest.getPinned());

        if (updateCompilationRequest.getTitle() != null)
            compilation.setTitle(updateCompilationRequest.getTitle());

        List<Event> events = Collections.emptyList();
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            if (events.size() != updateCompilationRequest.getEvents().size()) {
                throw new NotFoundException("Одно или несколько событий не найдены");
            }
        }
        compilation.setEvents(new HashSet<>(events));
        return compilation;
    }
}
