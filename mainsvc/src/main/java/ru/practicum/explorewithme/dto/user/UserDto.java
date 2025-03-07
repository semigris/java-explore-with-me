package ru.practicum.explorewithme.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /** Идентификатор */
    private Long id;

    /** Почтовый адрес */
    private String email;

    /** Имя */
    private String name;
}

