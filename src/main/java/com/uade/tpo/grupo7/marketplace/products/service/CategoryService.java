package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.mapper.CategoryMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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
        category.setParent(resolveParentCategory(dto.parentId()));

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, UpdateCategoryRequest dto) {
        Category category = getCategoryById(categoryId);

        if (dto.name() != null) {
            category.setName(dto.name());
        }

        if (dto.code() != null) {
            if (categoryRepository.existsByCodeAndIdNot(dto.code(), categoryId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Category code already exists"
                );
            }
            category.setCode(dto.code());
        }

        if (dto.parentId() != null) {
            if (dto.parentId().equals(categoryId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Category cannot be its own parent"
                );
            }

            category.setParent(resolveParentCategory(dto.parentId()));
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);
        List<Category> categoriesToDelete = new ArrayList<>();
        collectCategoriesForDeletion(category, categoriesToDelete);

        Set<Long> categoryIdsToDelete = categoriesToDelete.stream()
                .map(Category::getId)
                .collect(java.util.stream.Collectors.toSet());

        List<com.uade.tpo.grupo7.marketplace.products.entity.Product> products =
                productRepository.findDistinctByCategories_IdIn(categoryIdsToDelete);

        for (com.uade.tpo.grupo7.marketplace.products.entity.Product product : products) {
            if (product.getCategories() == null || product.getCategories().isEmpty()) {
                continue;
            }

            product.getCategories().removeIf(existingCategory -> categoryIdsToDelete.contains(existingCategory.getId()));
        }

        productRepository.saveAll(products);
        categoryRepository.deleteAll(categoriesToDelete);
    }

    private Category resolveParentCategory(Long parentId) {
        if (parentId == null) {
            return null;
        }

        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Parent category not found"
                ));
    }

    private void collectCategoriesForDeletion(Category category, List<Category> categoriesToDelete) {
        List<Category> children = categoryRepository.findAllByParentId(category.getId());

        for (Category child : children) {
            collectCategoriesForDeletion(child, categoriesToDelete);
        }

        categoriesToDelete.add(category);
    }
}
