package com.uade.tpo.grupo7.marketplace.products.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewLikeRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.mapper.ReviewLikeMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewLikeRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;

@Service
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    public ReviewLikeServiceImpl(ReviewLikeRepository reviewLikeRepository, ReviewRepository reviewRepository) {
        this.reviewLikeRepository = reviewLikeRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewLike createReviewLike(Long reviewId, CreateReviewLikeRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        reviewLikeRepository.findByReview_IdAndBuyerId(reviewId, request.getBuyerId())
                .ifPresent(existingLike -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "User already liked this review"
                    );
                });

        ReviewLike reviewLike = ReviewLikeMapper.toEntity(request, review);
        return reviewLikeRepository.save(reviewLike);
    }

    @Override
    public long countLikesByReviewId(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        return reviewLikeRepository.countByReview_Id(reviewId);
    }

    @Override
    public void deleteReviewLike(Long reviewId, Integer buyerId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        ReviewLike reviewLike = reviewLikeRepository.findByReview_IdAndBuyerId(reviewId, buyerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Like not found for review " + reviewId + " and user " + buyerId
                ));

        reviewLikeRepository.delete(reviewLike);
    }
}