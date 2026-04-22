package com.uade.tpo.grupo7.marketplace.products.mapper;

import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

public class ReviewLikeMapper {

    private ReviewLikeMapper() {
    }

    public static ReviewLike toEntity(Review review, User user) {
        return ReviewLike.builder()
                .review(review)
                .buyer(user)
                .build();
    }
}