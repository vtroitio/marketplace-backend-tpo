package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ReviewResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewService;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products/{productId}/reviews")
@Tag(name = "Reviews", description = "Endpoints de reviews de productos")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponse createReview(
            @PathVariable Long productId,
            @RequestBody @Valid CreateReviewRequest request,
            @AuthenticationPrincipal User user
    ) {
        return reviewService.createReview(productId, request, user);
    }
    
    @GetMapping
    public List<ReviewResponse> findAllByDeletedAtIsNullList(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PreAuthorize("""
        hasRole('BUYER') and 
        @ownership.isProductReviewOwner(#reviewId, authentication.principal)
    """)
    @PatchMapping("{reviewId}")
    public ReviewResponse updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateReviewRequest request,
            @AuthenticationPrincipal User user
    ) {
        return reviewService.updateReview(reviewId, request, user);
    }

    @PreAuthorize("""
        hasRole('BUYER') and 
        @ownership.isProductReviewOwner(#reviewId, authentication.principal)
    """)
    @DeleteMapping("{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user
    ) {
        reviewService.deleteReview(reviewId, user);
        return ResponseEntity.noContent().build();
    }
}
