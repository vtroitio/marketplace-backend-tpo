package com.uade.tpo.grupo7.marketplace.products.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewLikeRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewLikeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("products/{productId}/reviews/{reviewId}/likes")
@Tag(name = "Review Likes", description = "Endpoints para gestionar los 'likes' de las reseñas de los productos")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    public ReviewLikeController(ReviewLikeService reviewLikeService) {
        this.reviewLikeService = reviewLikeService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewLike createReviewLike(
            @PathVariable Long reviewId,
            @RequestBody @Valid CreateReviewLikeRequest request
    ) {
        return reviewLikeService.createReviewLike(reviewId, request);
    }

    @GetMapping("count")
    public long countLikesByReviewId(@PathVariable Long reviewId) {
        return reviewLikeService.countLikesByReviewId(reviewId);
    }

    @PreAuthorize("""
        hasRole('BUYER') and 
        @ownership.isProductReviewOwner(#reviewId, authentication.principal)
    """)
    @DeleteMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReviewLike(
            @PathVariable Long reviewId,
            @PathVariable("userId") Integer buyerId
    ) {
        reviewLikeService.deleteReviewLike(reviewId, buyerId);
    }
}