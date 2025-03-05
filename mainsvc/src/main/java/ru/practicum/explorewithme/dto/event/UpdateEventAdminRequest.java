package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.model.Location;

import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {

    /** Новая аннотация */
    @Size(min = 20, max = 2000, message = "Аннотация должна содержать от 20 до 2000 символов")
    private String annotation;

    /** Новая категория */
    private Long category;

    /** Новое описание */
    @Size(min = 20, max = 7000, message = "Описание должно содержать от 20 до 7000 символов")
    private String description;

    /** Новые дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss" */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime eventDate;

    /** Локация */
    private Location location;

    /** Новое значение флага о платности мероприятия */
    private Boolean paid;

    /** Новый лимит пользователей */
    private Long participantLimit;

    /** Нужна ли пре-модерация заявок на участие */
    private Boolean requestModeration;

    /** Новое состояние события */
    private AdminStateAction stateAction;

    /** Новый заголовок */
    @Size(min = 3, max = 120, message = "Заголовок должен содержать от 20 до 2000 символов")
    private String title;

    public enum AdminStateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }
}

