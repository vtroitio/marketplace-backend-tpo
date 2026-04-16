package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uade.tpo.grupo7.marketplace.products.dto.CategoryResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.mapper.CategoryMapper;
import com.uade.tpo.grupo7.marketplace.products.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("categories")
@Tag(name = "Categories", description = "Endpoints de categorías del marketplace")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(
        summary = "Obtener todas las categorías",
        description = "Obtiene la lista completa de categorías disponibles"
    )
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @GetMapping("{categoryId}")
    @Operation(
        summary = "Obtener una categoría por ID",
        description = "Obtiene una categoría específica por su identificador"
    )
    public CategoryResponse getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        return CategoryMapper.toResponse(category);
    }

    @PostMapping
    @Operation(
        summary = "Crear una nueva categoría",
        description = "Crea una nueva categoría en el marketplace"
    )
    public CategoryResponse createCategory(@Valid @RequestBody CreateCategoryRequest dto) {
        Category category = categoryService.createCategory(dto);
        return CategoryMapper.toResponse(category);
    }
}