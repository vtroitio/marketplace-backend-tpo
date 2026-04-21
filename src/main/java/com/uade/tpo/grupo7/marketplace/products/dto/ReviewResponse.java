package com.uade.tpo.grupo7.marketplace.products.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String description,
        Integer rating,
        LocalDateTime deletedAt
){}
