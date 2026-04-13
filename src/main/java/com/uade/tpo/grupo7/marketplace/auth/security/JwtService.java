package com.uade.tpo.grupo7.marketplace.auth.security;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

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
            .id(UUID.randomUUID().toString())
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

    public String extractUsername(String jwtToken) {
        return Jwts.parser()
            .verifyWith(this.getSignInKey())
            .build()
            .parseSignedClaims(jwtToken)
            .getPayload()
            .getSubject();
    }

    public boolean isTokenValid(String token, User user) {
        return (extractUsername(token).equals(user.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
            .verifyWith(this.getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }

}
