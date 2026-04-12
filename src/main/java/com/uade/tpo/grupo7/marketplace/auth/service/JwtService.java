package com.uade.tpo.grupo7.marketplace.auth.service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uade.tpo.grupo7.marketplace.users.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(User user) {
        return buildToken(user, this.accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, this.refreshTokenExpiration);
    }

    private String buildToken(User user, Long expiration) {
        return Jwts.builder()
            .id(user.getId().toString())
            .claims(Map.of("role", user.getRole().getCode()))
            .subject(user.getEmail())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(this.getSignInKey())
            .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
