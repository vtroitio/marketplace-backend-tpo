package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.ReviewLikeResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.mapper.ReviewLikeMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewLikeRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Service
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    public ReviewLikeServiceImpl(ReviewLikeRepository reviewLikeRepository, ReviewRepository reviewRepository) {
        this.reviewLikeRepository = reviewLikeRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
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

    @Override
    public long countLikesByReviewId(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Review with id " + reviewId + " not found"
        ));

        return reviewLikeRepository.countByReviewId(reviewId);
    }

    public ReviewLikeResponse getLikesByReviewId(Long reviewId, User user) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        long likeCount = reviewLikeRepository.countByReviewId(reviewId);

        boolean isLikedByCurrentUser = (user != null) && reviewLikeRepository.existsByReviewIdAndBuyer(reviewId, user);

        return new ReviewLikeResponse(reviewId, likeCount, isLikedByCurrentUser);
    }
}