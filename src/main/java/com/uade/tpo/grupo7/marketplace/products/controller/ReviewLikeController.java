package com.uade.tpo.grupo7.marketplace.products.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewLikeService;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@RestController
@RequestMapping("/reviews")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    public ReviewLikeController(ReviewLikeService reviewLikeService) {
        this.reviewLikeService = reviewLikeService;
    }

    @PostMapping("/{reviewId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createReviewLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        ReviewLike result = reviewLikeService.createReviewLike(reviewId, user);

        if (result == null) {
            return ResponseEntity.ok("Like eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
  }
    

    @GetMapping("/{reviewId}/likes/count")
    public long countLikesByReviewId(@PathVariable Long reviewId) {
        return reviewLikeService.countLikesByReviewId(reviewId);
    }

    @DeleteMapping("/{reviewId}/likes/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReviewLike(
            @PathVariable Long reviewId,
            @PathVariable User buyer
    ) {
        reviewLikeService.deleteReviewLike(reviewId, buyer);
    }
}