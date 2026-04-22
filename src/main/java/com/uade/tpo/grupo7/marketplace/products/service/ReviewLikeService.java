package com.uade.tpo.grupo7.marketplace.products.service;

import com.uade.tpo.grupo7.marketplace.products.dto.ReviewLikeResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

public interface ReviewLikeService {

    ReviewLike createReviewLike(Long reviewId, User user);

    long countLikesByReviewId(Long reviewId);

    ReviewLikeResponse getLikesByReviewId(Long reviewId, User user);

}
