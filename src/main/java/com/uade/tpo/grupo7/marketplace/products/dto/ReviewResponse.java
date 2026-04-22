package com.uade.tpo.grupo7.marketplace.products.dto;

public record ReviewResponse(
        Long id,
        Integer rating,
        String title,
        String description
){}
