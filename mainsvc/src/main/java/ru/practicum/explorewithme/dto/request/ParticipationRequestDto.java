package ru.practicum.explorewithme.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@Builder
public class ParticipationRequestDto {

    /** Идентификатор*/
    private Long id;

    /** Дата и время создания заявки */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    /** Идентификатор события */
    private Long event;

    /** Идентификатор пользователя, отправившего заявку */
    private Long requester;

    /** Статус заявки */
    private String status;
}