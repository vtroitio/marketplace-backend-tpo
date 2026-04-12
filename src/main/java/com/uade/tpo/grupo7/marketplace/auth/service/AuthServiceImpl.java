package com.uade.tpo.grupo7.marketplace.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.grupo7.marketplace.auth.dto.AuthResponse;
import com.uade.tpo.grupo7.marketplace.auth.dto.LoginRequest;
import com.uade.tpo.grupo7.marketplace.auth.dto.RegisterRequest;
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

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, JwtService jwtService, UserSessionRepository userSessionRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public AuthResponse register(RegisterRequest dto) {
        String passwordHash = this.passwordEncoder.encode(dto.password());
        Role buyerRole = this.roleRepository.findByCode(RoleCode.ROLE_BUYER)
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

        return new AuthResponse(accessToken);
    }

    @Override
    public AuthResponse login(LoginRequest dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    private void buildUserSession(User user, String refreshToken) {
        LocalDateTime expiresAt = LocalDateTime.now()
                .plus(Duration.ofMillis(this.refreshTokenExpiration));

        UserSession session = UserSession.builder()
                .user(user)
                .token(refreshToken)
                .familyId(UUID.randomUUID())
                .expiresAt(expiresAt)
                .build();

        this.userSessionRepository.save(session);
    }

}
