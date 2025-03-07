package ru.practicum.explorewithme.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
public class ParticipationRequest {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Событие */
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /** Пользователь, отправивший заявку */
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    /** Статус заявки */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    /** Дата и время создания заявки */
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime created;

    public enum RequestStatus {
        PENDING, CONFIRMED, REJECTED, CANCELED
    }
}
