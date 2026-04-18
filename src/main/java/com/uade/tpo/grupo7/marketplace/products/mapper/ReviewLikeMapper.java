package com.uade.tpo.grupo7.marketplace.products.mapper;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewLikeRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;

import java.time.LocalDateTime;

public class ReviewLikeMapper {

    public static ReviewLike toEntity(CreateReviewLikeRequest request, Integer reviewId) {
        return ReviewLike.builder()
                .reviewId(reviewId)
                .BuyerId(request.getBuyerId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
