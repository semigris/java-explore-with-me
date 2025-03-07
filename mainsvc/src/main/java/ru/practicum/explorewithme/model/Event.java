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
@Table(name = "events")
public class Event {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Краткое описание */
    @Column(length = 2000, nullable = false)
    private String annotation;

    /** Категория события */
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /** Количество одобренных заявок на участие в данном событии */
    private Long confirmedRequests;

    /** Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss") */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime createdOn;

    /** Полное описание события */
    @Column(length = 7000, name = "description", nullable = false)
    private String description;

    /** Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss") */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    /** Инициатор события */
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    /** Местоположение события */
    @JoinColumn(name = "location_id", nullable = false)
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Location location;

    /** Нужно ли оплачивать участие */
    private Boolean paid;

    /** Ограничение на количество участников. Значение 0 - означает отсутствие ограничения */
    private Long participantLimit;

    /** Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss") */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime publishedOn;

    /** Нужна ли пре-модерация заявок на участие */
    private Boolean requestModeration;

    /** Список состояний жизненного цикла события */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EventState state;

    /** Заголовок события */
    private String title;

    /** Количество просмотрев события */
    private Long views;

    public enum EventState {
        PENDING, PUBLISHED, CANCELED;
    }
}
