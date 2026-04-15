package com.uade.tpo.grupo7.marketplace.products.controller;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewLikeRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewLikeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    public ReviewLikeController(ReviewLikeService reviewLikeService) {
        this.reviewLikeService = reviewLikeService;
    }

    @PostMapping("/{reviewId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewLike createReviewLike(
            @PathVariable Integer reviewId,
            @RequestBody @Valid CreateReviewLikeRequest request
    ) {
        return reviewLikeService.createReviewLike(reviewId, request);
    }

    @GetMapping("/{reviewId}/likes/count")
    public long countLikesByReviewId(@PathVariable Integer reviewId) {
        return reviewLikeService.countLikesByReviewId(reviewId);
    }

    @DeleteMapping("/{reviewId}/likes/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReviewLike(
            @PathVariable Integer reviewId,
            @PathVariable Integer BuyerId
    ) {
        reviewLikeService.deleteReviewLike(reviewId, BuyerId);
    }
}