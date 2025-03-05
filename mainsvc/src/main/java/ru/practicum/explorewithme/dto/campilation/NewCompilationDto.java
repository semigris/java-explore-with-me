package ru.practicum.explorewithme.dto.campilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {

    /** Список идентификаторов событий входящих в подборку */
    private List<Long> events;

    /** Закреплена ли подборка на главной странице сайта */
    private Boolean pinned = false;

    /** Заголовок подборки */
    @NotBlank
    @Size(min = 1, max = 50, message = "Заголовок подборки должен содержать от 1 до 50 символов")
    private String title;
}