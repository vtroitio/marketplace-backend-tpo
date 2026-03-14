package com.uade.tpo.grupo9.marketplace.products.mapper;

import com.uade.tpo.grupo9.marketplace.products.dto.CreateProductDto;
import com.uade.tpo.grupo9.marketplace.products.entity.Product;

/**
 * Class responsible for mapping Product-related DTOs
 * to entities and vice versa.
 */
public class ProductMapper {

    /**
     * Converts a {@link CreateProductDto} into a {@link Product} entity.
     *
     * @param dto the data transfer object containing the product
     *            information provided by the client
     */
    public static Product toEntitiy(CreateProductDto dto) {
        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .build();
    }

}