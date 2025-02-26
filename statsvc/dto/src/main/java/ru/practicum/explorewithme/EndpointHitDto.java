package ru.practicum.explorewithme;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EndpointHitDto {
    /** Идентификатор записи */
    private Long id;

    /** Идентификатор сервиса для которого записывается информация */
    private String app;

    /** URI для которого был осуществлен запрос */
    private String uri;

    /** IP-адрес пользователя, осуществившего запрос */
    private String ip;

    /** Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss") */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
