package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import com.uade.tpo.grupo7.marketplace.products.dto.CategoryResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;

public interface CategoryService {

    List<CategoryResponse> getCategoryResponses();

    CategoryResponse getCategoryResponseById(Long categoryId);

    CategoryResponse createCategoryResponse(CreateCategoryRequest dto);

    CategoryResponse updateCategoryResponse(Long categoryId, UpdateCategoryRequest dto);

    List<Category> getCategories();

    Category getCategoryById(Long categoryId);

    Category createCategory(CreateCategoryRequest dto);

    Category updateCategory(Long categoryId, UpdateCategoryRequest dto);

    void deleteCategory(Long categoryId);
}
