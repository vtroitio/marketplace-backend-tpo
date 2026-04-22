package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.products.dto.CategoryResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("categories")
@Tag(name = "Categories", description = "Endpoints de categorias del marketplace")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(
        summary = "Obtener todas las categorias",
        description = "Obtiene la lista completa de categorias disponibles"
    )
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategoryResponses();
    }

    @GetMapping("{categoryId}")
    @Operation(
        summary = "Obtener una categoria por ID",
        description = "Obtiene una categoria especifica por su identificador"
    )
    public CategoryResponse getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryResponseById(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Crear una nueva categoria",
        description = "Crea una nueva categoria en el marketplace"
    )
    public CategoryResponse createCategory(@Valid @RequestBody CreateCategoryRequest dto) {
        return categoryService.createCategoryResponse(dto);
    }

    @PatchMapping("{categoryId}")
    @Operation(
        summary = "Actualizar una categoria",
        description = "Actualiza parcialmente una categoria existente"
    )
    public CategoryResponse updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody UpdateCategoryRequest dto
    ) {
        return categoryService.updateCategoryResponse(categoryId, dto);
    }

    @DeleteMapping("{categoryId}")
    @Operation(
        summary = "Eliminar una categoria",
        description = "Elimina una categoria si no tiene subcategorias ni productos asociados"
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
