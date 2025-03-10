package ru.practicum.explorewithme.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.campilation.CompilationDto;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.service.base.CategoryService;
import ru.practicum.explorewithme.service.base.CompilationService;
import ru.practicum.explorewithme.service.base.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.explorewithme.utils.Constant.DATE_FORMAT;

@Slf4j
@RestController
@AllArgsConstructor
public class MainPublicController {

    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final CategoryMapper categoryMapper;

    /**
     * Public: Категории
     *
     * GET /categories Получение категорий.
     */
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.debug("Запрос на получение категорий с параметрами: from={}, size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    /**
     * GET /categories/{catId} Получение информации о категории по её идентификатору.
     */

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.debug("Запрос на получение категории с id: {}", catId);
        return categoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }

    /**
     * Public: Подборки событий
     *
     * GET /compilations Получение подборок событий.
     */
    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.debug("Запрос на получение подборок с параметрами: pinned={}, from={}, size={}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     * GET /compilations/{compId} Получение подборки событий по его id.
     */
    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.debug("Запрос на получение подборки с id: {}", compId);
        return compilationService.getCompilationById(compId);
    }

    /**
     * Public: События
     *
     * GET /events Получение событий с возможностью фильтрации.
     */
    @GetMapping("/events")
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.debug("Запрос на получение событий с параметрами: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    /**
     * GET /events/{id} Получение подробной информации об опубликованном событии по его идентификатору.
     */
    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        log.debug("Запрос на получение подробной информации о событии с id: {}", id);
        return eventService.getEvent(id, request);
    }
}
