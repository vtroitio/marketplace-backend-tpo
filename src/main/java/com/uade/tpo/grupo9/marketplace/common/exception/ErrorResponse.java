package com.uade.tpo.grupo9.marketplace.common.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO de respuesta para errores en la API")
public record ErrorResponse(

    @Schema(description = "Código de estado HTTP del error", example = "400")
    int status,

    @Schema(description = "Mensaje descriptivo del error", example = "Validation errors")
    String message,

    @Schema(description = "Detalles adicionales sobre el error, como campos inválidos", example = "{\"name\": \"Name is required\", \"price\": \"Price must be positive\"}")  
    Map<String, String> errors,
    
    @Schema(description = "Método HTTP de la solicitud que causó el error", example = "POST")
    String method,
    
    @Schema(description = "Ruta de la solicitud que causó el error", example = "/products")
    String path
) {}