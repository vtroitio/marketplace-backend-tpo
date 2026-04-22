package com.uade.tpo.grupo7.marketplace.products.mapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.uade.tpo.grupo7.marketplace.products.dto.AttributeValueSummaryResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductVariantResponse;
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
                mapCategories(product),
                mapVariants(product)
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

    private static List<ProductVariantResponse> mapVariants(Product product) {
        if (product.getVariants() == null) {
            return List.of();
        }

        return product.getVariants().stream()
                .map(variant -> new ProductVariantResponse(
                        variant.getId(),
                        variant.getSku(),
                        variant.getPrice(),
                        variant.getStock(),
                        mapAttributeValues(variant.getAttributeValues())
                ))
                .toList();
    }

    private static List<AttributeValueSummaryResponse> mapAttributeValues(
        List<com.uade.tpo.grupo7.marketplace.products.entity.VariantAttributeValue> attributeValues
    ) {
        if (attributeValues == null) {
            return List.of();
        }

        return attributeValues.stream()
                .sorted(Comparator.comparing(v -> v.getAttributeValue().getAttribute().getName()))
                .map(variantAttributeValue -> {
                    var attributeValue = variantAttributeValue.getAttributeValue();
                    var attribute = attributeValue.getAttribute();
                    return new AttributeValueSummaryResponse(
                            attributeValue.getId(),
                            attributeValue.getValue(),
                            attributeValue.getCode(),
                            attributeValue.getHexColor(),
                            attribute.getId(),
                            attribute.getName(),
                            attribute.getCode()
                    );
                })
                .toList();
    }
}
