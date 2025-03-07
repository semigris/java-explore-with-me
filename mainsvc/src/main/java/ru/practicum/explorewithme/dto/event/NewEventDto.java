package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.practicum.explorewithme.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;

@Data
public class NewEventDto {

    /** Краткое описание события */
    @NotBlank
    @Size(min = 20, max = 2000, message = "Аннотация должна содержать от 20 до 2000 символов")
    private String annotation;

    /** id категории к которой относится событие */
    @NotNull
    private Long category;

    /** Полное описание события */
    @NotBlank(message = "Полное описание события не должно быть пустым или состоять только из пробелов")
    @Size(min = 20, max = 7000, message = "Описание должно содержать от 20 до 7000 символов")
    private String description;

    /** Дата и время на которые намечено событие */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    @FutureOrPresent
    private LocalDateTime eventDate;

    /** Местоположение события */
    @NotNull
    private Location location;

    /** Нужно ли оплачивать участие в событии */
    private boolean paid = false;

    /** Ограничение на количество участников */
    @Min(0)
    private Long participantLimit = 0L;

    /** Нужна ли пре-модерация заявок на участие */
    private boolean requestModeration = true;

    /** Заголовок события */
    @NotBlank
    @Size(min = 3, max = 120, message = "Заголовок события должен содержать от 3 до 120 символов")
    private String title;
}
