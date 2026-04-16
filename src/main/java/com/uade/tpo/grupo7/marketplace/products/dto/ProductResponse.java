package com.uade.tpo.grupo7.marketplace.products.dto;

public record ProductResponse(
    Long id,
    String name,
    Double price,
    String description
) {}
