package com.uade.tpo.grupo7.marketplace.products.mapper;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ReviewResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

public class ReviewMapper {

    private ReviewMapper() {}

    public static Review toEntity(CreateReviewRequest request, Product product, User user) {
        return Review.builder()
                .product(product)
                .buyer(user)
                .rating(request.rating())
                .title(request.title())
                .description(request.description())
                .deletedAt(null)
                .build();
    }

    public static ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getTitle(),
                review.getDescription()
        );
    }
}