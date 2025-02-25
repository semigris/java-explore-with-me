package ru.practicum.explorewithme.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "stats")
public class EndpointHit {
    /** Идентификатор записи */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Идентификатор сервиса для которого записывается информация */
    @Column(name = "app")
    private String app;

    /** URI для которого был осуществлен запрос */
    @Column(name = "uri")
    private String uri;

    /** IP-адрес пользователя, осуществившего запрос */
    @Column(name = "ip")
    private String ip;

    /** Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss") */
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}


