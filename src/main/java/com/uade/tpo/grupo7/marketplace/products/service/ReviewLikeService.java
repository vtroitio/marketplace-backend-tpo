package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.mapper.ReviewLikeMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewLikeRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Service
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    public ReviewLikeService(ReviewLikeRepository reviewLikeRepository, ReviewRepository reviewRepository) {
        this.reviewLikeRepository = reviewLikeRepository;
        this.reviewRepository = reviewRepository;
    }

    public ReviewLike createReviewLike(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByReviewIdAndBuyer(reviewId, user);
        if (existingLike.isPresent()) {
            reviewLikeRepository.delete(existingLike.get());
            return null;
        } else {
            ReviewLike reviewLike = ReviewLikeMapper.toEntity(review, user);
            return reviewLikeRepository.save(reviewLike);
        }
    }

    public long countLikesByReviewId(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        return reviewLikeRepository.countByReviewId(reviewId);
    }

    public void deleteReviewLike(Long reviewId, User user) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Review with id " + reviewId + " not found"
                ));

        ReviewLike reviewLike = reviewLikeRepository.findByReviewIdAndBuyer(reviewId, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Like not found for review " + reviewId + " and user " + user.getId()
                ));

        reviewLikeRepository.delete(reviewLike);
    }
}