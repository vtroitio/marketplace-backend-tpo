package com.uade.tpo.grupo7.marketplace.products.mapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.uade.tpo.grupo7.marketplace.products.dto.AttributeValueSummaryResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductDetailResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductOwnerResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductVariantResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;

public class ProductMapper {

    public static Product toEntitiy(CreateProductRequest dto) {
        return Product.builder()
                .name(dto.name())
                .price(dto.price())
                .description(dto.description())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ProductDetailResponse toDetailResponse(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCoverImagePath(),
                product.getVariants().stream()
                    .mapToInt(ProductVariant::getStock)
                    .sum(),
                product.isActive(),
                mapOwner(product),
                mapCategories(product),
                mapVariants(product)
        );
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCoverImagePath(),
                product.getVariants().stream()
                    .mapToInt(ProductVariant::getStock)
                    .sum(),
                product.getVariants().size(),
                product.isActive()
                // mapCategories(product),
                // mapVariants(product)
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
                        variant.getImages(),
                        mapAttributeValues(variant.getAttributeValues())))
                .toList();
    }

    private static List<AttributeValueSummaryResponse> mapAttributeValues(
            List<com.uade.tpo.grupo7.marketplace.products.entity.VariantAttributeValue> attributeValues) {
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
                            attribute.getCode());
                })
                .toList();
    }

    private static ProductOwnerResponse mapOwner(Product product) {
        if (product.getSeller() == null) {
            return null;
        }

        return new ProductOwnerResponse(
                product.getSeller().getId(),
                product.getSeller().getUsername()
        );
    }
}
