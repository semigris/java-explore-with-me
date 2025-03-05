package ru.practicum.explorewithme.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.campilation.CompilationDto;
import ru.practicum.explorewithme.dto.campilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.repository.EventRepository;

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
        List<Event> events = null;
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }

        return Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}
