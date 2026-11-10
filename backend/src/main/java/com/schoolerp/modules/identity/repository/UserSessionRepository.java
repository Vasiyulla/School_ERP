package com.schoolerp.modules.identity.repository;

import com.schoolerp.modules.identity.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    Optional<UserSession> findByRefreshTokenHashAndRevokedFalse(String refreshTokenHash);
    List<UserSession> findByTenantIdAndUserEmailAndRevokedFalseOrderByCreatedAtDesc(String tenantId, String userEmail);
    List<UserSession> findByTenantIdAndRevokedFalseOrderByCreatedAtDesc(String tenantId);
}

