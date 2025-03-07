package ru.practicum.explorewithme.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;


@Data
@Builder
public class ParticipationRequestDto {

    /** Идентификатор */
    private Long id;

    /** Дата и время создания заявки */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime created;

    /** Идентификатор события */
    private Long event;

    /** Идентификатор пользователя, отправившего заявку */
    private Long requester;

    /** Статус заявки */
    private String status;
}
