package com.uade.tpo.grupo7.marketplace.products.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.products.dto.ReviewLikeResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewLikeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

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
    public ResponseEntity<?> createReviewLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user
    ) {
        ReviewLike result = reviewLikeService.createReviewLike(reviewId, user);

        if (result == null) {
            return ResponseEntity.ok("Like eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
    }

    @GetMapping("count")
    public ReviewLikeResponse getLikesByReviewId(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user
    ) {
        return reviewLikeService.getLikesByReviewId(reviewId, user);
    }
}