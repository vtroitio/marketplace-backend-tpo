package com.uade.tpo.grupo7.marketplace.auth.sessions.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.grupo7.marketplace.auth.sessions.entity.UserSession;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByToken(String jwtToken);

    List<UserSession> findAllByFamilyId(UUID familyId);

    void deleteAllByFamilyId(UUID familyId);
}
