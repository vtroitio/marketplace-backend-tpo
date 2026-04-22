package com.uade.tpo.grupo7.marketplace.products.service;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewLikeRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;

public interface ReviewLikeService {

    ReviewLike createReviewLike(Long reviewId, CreateReviewLikeRequest request);

    long countLikesByReviewId(Long reviewId);

    void deleteReviewLike(Long reviewId, Integer buyerId);
}