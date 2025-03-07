package ru.practicum.explorewithme.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    /** Список стектрейсов или описания ошибок */
    private List<String> errors;

    /** Сообщение об ошибке */
    private String message;

    /** Общее описание причины ошибки */
    private String reason;

    /** Код статуса HTTP-ответа */
    private String status;

    /** Дата и время ошибки */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    private LocalDateTime timestamp;
}
