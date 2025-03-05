package ru.practicum.explorewithme.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {

    /** Идентификатор */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Список событий входящих в подборку */
    @OneToMany
    @JoinColumn(name = "compilation_id")
    private List<Event> events;

    /** Закреплена ли подборка на главной странице сайта */
    private Boolean pinned;

    /** Заголовок подборки */
    private String title;
}

