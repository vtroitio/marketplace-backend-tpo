package com.uade.tpo.grupo7.marketplace.products.service;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewLikeRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.mapper.ReviewLikeMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewLikeRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    public ReviewLikeService(ReviewLikeRepository reviewLikeRepository, ReviewRepository reviewRepository) {
        this.reviewLikeRepository = reviewLikeRepository;
        this.reviewRepository = reviewRepository;
    }

    public ReviewLike createReviewLike(Integer reviewId, CreateReviewLikeRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        reviewLikeRepository.findByReviewIdAndUserId(review.getId(), request.getBuyerId())
                .ifPresent(reviewLike -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "User already liked this review"
                    );
                });

        ReviewLike reviewLike = ReviewLikeMapper.toEntity(request, review.getId());
        return reviewLikeRepository.create(reviewLike);
    }

    public long countLikesByReviewId(Integer reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        return reviewLikeRepository.countByReviewId(reviewId);
    }

    public void deleteReviewLike(Integer reviewId, Integer BuyerId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, BuyerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Like not found for review " + reviewId + " and user " + BuyerId
                ));

        reviewLikeRepository.delete(reviewLike);
    }
}