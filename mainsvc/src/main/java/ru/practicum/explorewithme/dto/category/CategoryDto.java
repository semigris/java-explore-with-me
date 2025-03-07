package ru.practicum.explorewithme.dto.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CategoryDto {

    /** Идентификатор категории */
    private Long id;

    /** Название категории */
    @Size(min = 1, max = 50, message = "Название категории должно содержать от 1 до 50 символов")
    private String name;
}
