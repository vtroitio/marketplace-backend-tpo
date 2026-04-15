package com.uade.tpo.grupo7.marketplace.products.mapper;

import java.time.LocalDateTime;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;

/**
 * Class responsible for mapping Product-related DTOs
 * to entities and vice versa.
 */
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
                product.getCreatedAt(),
                product.getDeletedAt()
        );
    }
}