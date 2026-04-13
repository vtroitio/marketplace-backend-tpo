package com.uade.tpo.grupo7.marketplace.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.auth.domain.AuthTokens;
import com.uade.tpo.grupo7.marketplace.auth.dto.LoginRequest;
import com.uade.tpo.grupo7.marketplace.auth.dto.RegisterRequest;
import com.uade.tpo.grupo7.marketplace.auth.security.JwtService;
import com.uade.tpo.grupo7.marketplace.auth.sessions.entity.UserSession;
import com.uade.tpo.grupo7.marketplace.auth.sessions.repository.UserSessionRepository;
import com.uade.tpo.grupo7.marketplace.common.enums.RoleCode;
import com.uade.tpo.grupo7.marketplace.users.entity.Role;
import com.uade.tpo.grupo7.marketplace.users.entity.User;
import com.uade.tpo.grupo7.marketplace.users.repository.RoleRepository;
import com.uade.tpo.grupo7.marketplace.users.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            JwtService jwtService,
            UserSessionRepository userSessionRepository,
            AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userSessionRepository = userSessionRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthTokens register(RegisterRequest dto) {

        if (this.userRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        if (this.userRepository.existsByUsername(dto.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
        }

        String passwordHash = this.passwordEncoder.encode(dto.password());
        Role buyerRole = this.roleRepository.findByCode(RoleCode.BUYER)
                .orElseThrow(() -> new RuntimeException("Buyer role not found"));

        User user = User.builder()
                .email(dto.email())
                .username(dto.username())
                .passwordHash(passwordHash)
                .name(dto.name())
                .surname(dto.surname())
                .role(buyerRole)
                .build();

        User savedUser = this.userRepository.save(user);
        String accessToken = this.jwtService.generateAccessToken(savedUser);
        String refreshToken = this.jwtService.generateRefreshToken(savedUser);

        this.buildUserSession(savedUser, refreshToken);

        return new AuthTokens(accessToken, refreshToken);
    }

    @Override
    public AuthTokens login(LoginRequest dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()));

        User user = this.userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String accessToken = this.jwtService.generateAccessToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);

        this.buildUserSession(user, refreshToken);

        return new AuthTokens(accessToken, refreshToken);
    }

    public Optional<AuthTokens> refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is required");
        }

        final UserSession currentSession = this.userSessionRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (currentSession.getRevokedAt() != null) {
            return Optional.empty();
        }

        final String userEmail = this.jwtService.extractUsername(refreshToken);

        if (userEmail == null || userEmail.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        final User user = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!this.jwtService.isTokenValid(refreshToken, user)) {
            return Optional.empty();
        }

        String newAccessToken = this.jwtService.generateAccessToken(user);
        String newRefreshToken = this.jwtService.generateRefreshToken(user);

        currentSession.setRevokedAt(LocalDateTime.now());
        this.userSessionRepository.save(currentSession);
        rotateUserSession(user, currentSession, newRefreshToken);

        return Optional.of(new AuthTokens(newAccessToken, newRefreshToken));
    }

    @Transactional
    @Override
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            System.out.println("Logout failed: Refresh token is required");
            return;
        }

        final UserSession currentSession = this.userSessionRepository.findByToken(refreshToken)
                .orElse(null);

        if (currentSession == null) {
            System.out.println("Logout failed: Session not found");
            return;
        }

        System.out.println("Logging out session with family ID: " + currentSession.getFamilyId());

        UUID familyId = currentSession.getFamilyId();
        this.userSessionRepository.deleteAllByFamilyId(familyId);
    }

    private void rotateUserSession(User user, UserSession currentSession, String newRefreshToken) {
        LocalDateTime expiresAt = this.jwtService.extractExpiration(newRefreshToken).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        UserSession session = UserSession.builder()
                .user(user)
                .token(newRefreshToken)
                .familyId(currentSession.getFamilyId())
                .expiresAt(expiresAt)
                .build();

        this.userSessionRepository.save(session);
    }

    private void buildUserSession(User user, String refreshToken) {
        LocalDateTime expiresAt = this.jwtService.extractExpiration(refreshToken).toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        UserSession session = UserSession.builder()
                .user(user)
                .token(refreshToken)
                .familyId(UUID.randomUUID())
                .expiresAt(expiresAt)
                .build();

        this.userSessionRepository.save(session);
    }

}
