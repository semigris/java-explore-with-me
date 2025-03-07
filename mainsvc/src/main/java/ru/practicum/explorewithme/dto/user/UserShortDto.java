package ru.practicum.explorewithme.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDto {

    /** Идентификатор */
    private Long id;

    /** Имя */
    private String name;
}

