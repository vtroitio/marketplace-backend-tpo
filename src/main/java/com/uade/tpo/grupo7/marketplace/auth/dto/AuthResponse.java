package com.uade.tpo.grupo7.marketplace.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
    @JsonProperty("access_token")
    String accessToken
) {}
