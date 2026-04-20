package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ReviewResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewService;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

<<<<<<< HEAD
    // @PostMapping("/{productId}/reviews")
    // @ResponseStatus(HttpStatus.CREATED)
    // public Review createReview(
    //         @PathVariable Integer productId,
    //         @RequestBody @Valid CreateReviewRequest request
    // ) {
    //     // return reviewService.createReview(productId, request);
    // }

    // @GetMapping("/{productId}/reviews")
    // public List<Review> getReviewsByProductId(@PathVariable Integer productId) {
    //     return reviewService.getReviewsByProductId(productId);
    // }
} 
=======
    @PostMapping("/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Review createReview(
            @PathVariable Long productId,
            @RequestBody @Valid CreateReviewRequest request,
            @AuthenticationPrincipal User user
    ) {

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        return reviewService.createReview(productId, request, user);
    }

    @GetMapping("/{productId}/reviews")
    public List<Review> findAllByDeletedAtIsNullList(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PutMapping("/reviews/{reviewId}")
    public Review updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateReviewRequest request,
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        return reviewService.updateReview(reviewId, request, user);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Review deletedReview = reviewService.deleteReview(reviewId, user);
        ReviewResponse response = new ReviewResponse(
                deletedReview.getId(),
                deletedReview.getDescription(),
                deletedReview.getRating(),
                deletedReview.getDeletedAt()
        );

        return ResponseEntity.ok(response);
    }
}
>>>>>>> 96859d5607ab2f261f3a5a458692b3c5e67047ee
