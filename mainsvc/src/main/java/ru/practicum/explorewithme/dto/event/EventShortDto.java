package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    /** Идентификатор */
    private Long id;

    /** Краткое описание */
    private String annotation;

    /** Категория события */
    private CategoryDto category;

    /** Количество одобренных заявок на участие в данном событии */
    private Long confirmedRequests;

    /** Дата и время на которые намечено событие */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    /** Инициатор события */
    private UserShortDto initiator;

    /** Нужно ли оплачивать участие */
    private Boolean paid;

    /** Заголовок */
    private String title;

    /** Количество просмотрев события */
    private Long views;
}
