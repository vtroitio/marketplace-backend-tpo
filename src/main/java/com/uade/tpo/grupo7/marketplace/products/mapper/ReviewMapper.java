package com.uade.tpo.grupo7.marketplace.products.mapper;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;

public class ReviewMapper {

    private ReviewMapper() {
    }

    public static Review toEntity(CreateReviewRequest request, Product product) {
        return Review.builder()
                .product(product)
                .buyerId(request.getBuyerId())
                .rating(request.getRating())
                .title(request.getTitle())
                .description(request.getDescription())
                .deletedAt(null)
                .build();
    }
}