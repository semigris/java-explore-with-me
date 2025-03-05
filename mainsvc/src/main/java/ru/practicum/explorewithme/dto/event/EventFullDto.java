package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.Location;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    /** Идентификатор */
    private Long id;

    /** Краткое описание */
    private String annotation;

    /** Категория события */
    private CategoryDto category;

    /** Количество одобренных заявок на участие в данном событии */
    private Long confirmedRequests;

    /** Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss") */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /** Полное описание события */
    private String description;

    /** Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss") */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /** Инициатор события */
    private UserShortDto initiator;

    /** Местоположение события */
    private Location location;

    /** Нужно ли оплачивать участие */
    private Boolean paid;

    /** Ограничение на количество участников. Значение 0 - означает отсутствие ограничения */
    private Long participantLimit;

    /** Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss") */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    /** Нужна ли пре-модерация заявок на участие */
    private Boolean requestModeration;

    /** Список состояний жизненного цикла события */
    private Event.EventState state;

    /** Заголовок события */
    private String title;

    /** Количество просмотрев события */
    private Long views;
}

