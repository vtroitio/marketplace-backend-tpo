package com.uade.tpo.grupo7.marketplace.products.mapper;

import java.time.LocalDateTime;
import java.util.List;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;

public class ProductMapper {

    public static Product toEntitiy(CreateProductRequest dto) {
        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .description(dto.description())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                mapCategories(product)
        );
    }

    private static List<com.uade.tpo.grupo7.marketplace.products.dto.CategoryResponse> mapCategories(Product product) {
        if (product.getCategories() == null) {
            return List.of();
        }

        return product.getCategories().stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }
}
