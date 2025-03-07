package ru.practicum.explorewithme.dto.campilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;

    /** Список событий входящих в подборку */
    private List<EventShortDto> events;

    /** Закреплена ли подборка на главной странице сайта */
    private Boolean pinned;

    /** Заголовок подборки */
    private String title;
}
