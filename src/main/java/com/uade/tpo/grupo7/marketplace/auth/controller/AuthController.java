package com.uade.tpo.grupo7.marketplace.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.auth.domain.AuthTokens;
import com.uade.tpo.grupo7.marketplace.auth.dto.AuthResponse;
import com.uade.tpo.grupo7.marketplace.auth.dto.LoginRequest;
import com.uade.tpo.grupo7.marketplace.auth.dto.RegisterRequest;
import com.uade.tpo.grupo7.marketplace.auth.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("auth")
@Tag(name = "Auth", description = "Endpoints de autenticación y autorización del marketplace")
public class AuthController {

    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest dto) {        
        AuthTokens tokens = this.authService.register(dto);
        
        ResponseCookie refreshCookie = buildRefreshCookie(tokens.refreshToken());

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshCookie.toString())
                .body(new AuthResponse(tokens.accessToken()));
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest dto) {
        AuthTokens tokens = this.authService.login(dto);

        ResponseCookie refreshCookie = buildRefreshCookie(tokens.refreshToken());

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshCookie.toString())
                .body(new AuthResponse(tokens.accessToken()));
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(required = false) String refreshToken) {
        Optional<AuthTokens> tokens = this.authService.refresh(refreshToken);

        if (tokens.isEmpty()) {
            ResponseCookie deleteCookie = clearRefreshTokenCookie();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Set-Cookie", deleteCookie.toString())
                    .build();
        }

        ResponseCookie refreshCookie = buildRefreshCookie(tokens.get().refreshToken());

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshCookie.toString())
                .body(new AuthResponse(tokens.get().accessToken()));
    }

    private ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/auth/refresh")
                .maxAge(Duration.ZERO)
                .sameSite("Strict")
                .build();
    }

    private ResponseCookie buildRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/auth/refresh")
                .maxAge(Duration.ofMillis(this.refreshTokenExpiration))
                .sameSite("Strict")
                .build();
    }

}
