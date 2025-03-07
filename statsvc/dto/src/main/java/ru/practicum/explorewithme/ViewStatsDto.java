package ru.practicum.explorewithme;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ViewStatsDto {
    /** Название сервиса */
    private String app;

    /** URI сервиса */
    private String uri;

    /** Количество просмотров */
    private Long hits;
}
