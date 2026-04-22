package com.uade.tpo.grupo7.marketplace.products.dto;

public record ReviewLikeResponse(
    Long reviewId,
    Long likeCount,
    Boolean islikedByCurrentUser
) {}
