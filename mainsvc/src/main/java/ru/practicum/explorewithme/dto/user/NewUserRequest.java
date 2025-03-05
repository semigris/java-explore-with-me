package ru.practicum.explorewithme.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    /** Почтовый адрес */
    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "Почтовый адрес должен содержать от 6 до 254 символов")
    private String email;

    /** Имя */
    @NotBlank
    @Size(min = 2, max = 250, message = "Имя должно содержать от 2 до 250 символов")
    private String name;
}
