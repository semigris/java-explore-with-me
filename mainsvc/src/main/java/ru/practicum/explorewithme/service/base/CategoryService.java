package ru.practicum.explorewithme.service.base;

import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.model.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long categoryId);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);
}
