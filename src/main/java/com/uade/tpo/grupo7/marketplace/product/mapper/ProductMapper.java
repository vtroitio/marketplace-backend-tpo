package com.uade.tpo.grupo7.marketplace.product.mapper;

import com.uade.tpo.grupo7.marketplace.product.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.product.entity.Product;

/**
 * Class responsible for mapping Product-related DTOs
 * to entities and vice versa.
 */
public class ProductMapper {

    /**
     * Converts a {@link CreateProductRequest} into a {@link Product} entity.
     *
     * @param dto the data transfer object containing the product
     *            information provided by the client
     */
    public static Product toEntitiy(CreateProductRequest dto) {
        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .build();
    }

}