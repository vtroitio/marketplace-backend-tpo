package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.mapper.CategoryMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found"
                ));
    }

    public Category createCategory(CreateCategoryRequest dto) {
        if (categoryRepository.existsByCode(dto.code())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Category code already exists"
            );
        }

        Category category = CategoryMapper.toEntity(dto);

        if (dto.parentId() != null) {
            Category parentCategory = categoryRepository.findById(dto.parentId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent category not found"
                    ));
            category.setParent(parentCategory);
        }

        return categoryRepository.save(category);
    }
}
