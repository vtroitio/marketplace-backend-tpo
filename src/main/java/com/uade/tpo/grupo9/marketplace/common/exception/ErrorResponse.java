package com.uade.tpo.grupo9.marketplace.common.exception;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para errores en la API")
public record ErrorResponse(

    @Schema(description = "Código de estado HTTP del error", example = "404")
    int status,

    @Schema(description = "Mensaje descriptivo del error", example = "Product not found")
    String message,

    @Schema(description = "Detalles adicionales sobre el error, como campos inválidos", example = "{\"name\": \"Name is required\"}")  
    Map<String, String> errors,
    
    @Schema(description = "Método HTTP de la solicitud que causó el error", example = "GET")
    String method,
    
    @Schema(description = "Ruta de la solicitud que causó el error", example = "/products/123")
    String path
) {}