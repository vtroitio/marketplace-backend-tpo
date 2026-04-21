package com.uade.tpo.grupo7.marketplace.products.mapper;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewLikeRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;

public class ReviewLikeMapper {

    private ReviewLikeMapper() {
    }

    public static ReviewLike toEntity(CreateReviewLikeRequest request, Review review) {
        return ReviewLike.builder()
                .review(review)
                .buyerId(request.getBuyerId())
                .build();
    }
}