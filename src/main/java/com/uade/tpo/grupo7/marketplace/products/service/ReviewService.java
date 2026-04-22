package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ReviewResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateReviewRequest;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

public interface ReviewService {

    ReviewResponse createReview(Long productId, CreateReviewRequest request, User user);

    List<ReviewResponse> getReviewsByProductId(Long productId);

    ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, User user);

    void deleteReview(Long reviewId, User user);
}
