package com.uade.tpo.grupo7.marketplace.products.dto;

import java.time.LocalDateTime;

public record ProductResponse(
    Integer id,
    String name,
    Double price,
    String description,
    LocalDateTime createdAt,
    LocalDateTime deletedAt
) {}