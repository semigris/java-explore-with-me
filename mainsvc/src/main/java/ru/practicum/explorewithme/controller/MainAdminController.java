package ru.practicum.explorewithme.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.campilation.CompilationDto;
import ru.practicum.explorewithme.dto.campilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.campilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.service.base.CategoryService;
import ru.practicum.explorewithme.service.base.CompilationService;
import ru.practicum.explorewithme.service.base.EventService;
import ru.practicum.explorewithme.service.base.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class MainAdminController {

    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final UserService userService;

    /**
     * Admin: Категории
     * <p>
     * POST /admin/categories Добавление новой категории.
     * Обратите внимание: имя категории должно быть уникальным
     */
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.debug("Запрос на добавление категории: {}", newCategoryDto);
        return categoryService.addCategory(newCategoryDto);
    }

    /**
     * DELETE /admin/categories/{catId} Удаление категории.
     */

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.debug("Запрос на удаление категории с id: {}", catId);
        categoryService.deleteCategory(catId);
    }

    /**
     * PATCH /admin/categories/{catId} Изменение категории.
     */

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        log.debug("Запрос на обновление категории с id: {}. Новые данные: {}", catId, categoryDto);
        return categoryService.updateCategory(catId, categoryDto);
    }

    /**
     * Admin: События
     * <p>
     * GET /admin/events Поиск событий. Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия.
     */

    @GetMapping("/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Запрос на получение событий с параметрами: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /** PATCH /admin/events/{eventId} Редактирование данных события и его статуса (отклонение/публикация). */
    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("Запрос на обновление события с id: {}. Новые данные: {}", eventId, updateEventAdminRequest);
        return eventService.updateEvent(eventId, updateEventAdminRequest);
    }

    /**
     * Admin: Пользователи
     * <p>
     * GET /admin/users Получение информации о пользователях.
     */

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Запрос на получение пользователей с параметрами: ids={}, from={}, size={}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    /** POST /admin/users Добавление нового пользователя. */

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.debug("Запрос на регистрацию пользователя: {}", newUserRequest);
        return userService.registerUser(newUserRequest);
    }

    /** DELETE /admin/users/{userId} Удаление пользователя. */

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.debug("Запрос на удаление пользователя с id: {}", userId);
        userService.deleteUser(userId);
    }

    /**
     * Admin: Подборки событий
     * <p>
     * POST /admin/compilations Добавление новой подборки (подборка может не содержать событий).
     */

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.debug("Запрос на добавление подборки: {}", newCompilationDto);
        return compilationService.addCompilation(newCompilationDto);
    }

    /** DELETE /admin/compilations/{compId} Удаление подборки. */

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.debug("Запрос на удаление подборки с id: {}", compId);
        compilationService.deleteCompilation(compId);
    }

    /** PATCH /admin/compilations/{compId} Обновить информацию о подборке. */

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.debug("Запрос на обновление подборки с id: {}. Новые данные: {}", compId, updateCompilationRequest);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
