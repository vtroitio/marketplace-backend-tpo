package com.uade.tpo.grupo7.marketplace.products.mapper;

import com.uade.tpo.grupo7.marketplace.products.dto.CategoryResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateCategoryRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;

public class CategoryMapper {

    public static Category toEntity(CreateCategoryRequest dto) {
        return Category.builder()
                .name(dto.name())
                .code(dto.code())
                .build();
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getCode(),
                category.getParent() != null ? category.getParent().getId() : null
        );
    }
}
