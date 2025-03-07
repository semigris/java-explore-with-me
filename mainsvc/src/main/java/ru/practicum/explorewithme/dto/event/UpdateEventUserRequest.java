package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;

@Data
public class UpdateEventUserRequest {

    /** Новая аннотация */
    @Size(min = 20, max = 2000, message = "Аннотация должна содержать от 20 до 2000 символов")
    private String annotation;

    /** Новая категория */
    @Positive(message = "id категории не может быть отрицательным числом")
    private Long category;

    /** Новое описание */
    @Size(min = 20, max = 7000, message = "Описание должно содержать от 20 до 7000 символов")
    private String description;

    /** Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss" */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    /** Местоположение */
    private Location location;

    /** Новое значение флага о платности мероприятия */
    private Boolean paid;

    /** Новый лимит пользователей */
    @PositiveOrZero(message = "Ограничение на количество участников не может быть отрицательным числом")
    private Long participantLimit;

    /** Нужна ли пре-модерация заявок на участие */
    private Boolean requestModeration;

    /** Изменение состояния события */
    private UserStateAction stateAction;

    /** Новый заголовок */
    @Size(min = 3, max = 120, message = "Заголовок должен содержать от 3 до 120 символов")
    private String title;

    public enum UserStateAction {
        SEND_TO_REVIEW, CANCEL_REVIEW
    }
}
