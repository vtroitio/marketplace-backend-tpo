package com.uade.tpo.grupo7.marketplace.products.mapper;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;

import java.time.LocalDateTime;

public class ReviewMapper {

    public static Review toEntity(CreateReviewRequest request, Integer productId) {
        return Review.builder()
                .productId(productId)
                .buyerId(request.getBuyerId())
                .rating(request.getRating())
                .title(request.getTitle())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();
    }
}