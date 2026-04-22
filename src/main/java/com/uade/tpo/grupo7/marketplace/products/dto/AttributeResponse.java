package com.uade.tpo.grupo7.marketplace.products.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para atributos de variantes")
public record AttributeResponse(
    Long id,
    String name,
    String code,
    List<AttributeValueResponse> values
) {}
