package com.uade.tpo.grupo7.marketplace.auth.domain;

public record AuthTokens(
    String accessToken,
    String refreshToken
) {}
