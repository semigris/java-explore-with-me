package ru.practicum.explorewithme.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class ViewStats {
    /** Название сервиса */
    private String app;

    /** URI сервиса */
    private String uri;

    /** Количество просмотров */
    private Long hits;
}
