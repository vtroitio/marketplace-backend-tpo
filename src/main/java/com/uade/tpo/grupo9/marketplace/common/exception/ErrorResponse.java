package com.uade.tpo.grupo9.marketplace.common.exception;

import java.util.Map;

public record ErrorResponse(
    int status,
    String message,
    Map<String, String> errors,
    String method,
    String path
) {}