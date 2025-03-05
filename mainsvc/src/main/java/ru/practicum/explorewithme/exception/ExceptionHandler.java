package ru.practicum.explorewithme.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        log.error("Error 500: {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Внутренняя ошибка сервера")
                .status("INTERNAL_SERVER_ERROR")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.error("Error 409: {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Конфликт при обработке запроса")
                .status("CONFLICT")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.error("Error 404: {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Запрашиваемый ресурс не найден")
                .status("NOT_FOUND")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            BadRequestException.class,
            MethodArgumentNotValidException.class,
            InvalidDataAccessApiUsageException.class,
            PropertyValueException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestExceptions(RuntimeException e) {
        log.error("Error 400: {}", e.getMessage());
        return ApiError.builder()
                .message(e.getMessage())
                .reason("Неверный формат или содержимое запроса")
                .status("BAD_REQUEST")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
