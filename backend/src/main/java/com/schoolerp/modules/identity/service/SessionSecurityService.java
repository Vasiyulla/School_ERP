package com.schoolerp.modules.identity.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.identity.dto.AuthResponse;
import com.schoolerp.modules.identity.entity.Role;
import com.schoolerp.modules.identity.entity.User;
import com.schoolerp.modules.identity.entity.UserSession;
import com.schoolerp.modules.identity.repository.UserRepository;
import com.schoolerp.modules.identity.repository.UserSessionRepository;
import com.schoolerp.modules.institution.entity.Tenant;
import com.schoolerp.modules.institution.repository.TenantRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionSecurityService {

    private final UserSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final JwtService jwtService;
    private final AuditLogService auditLogService;

    @Transactional
    public String issueRefreshSession(User user, String tenantId, String deviceName, String ipAddress) {
        String rawRefreshToken = "rt_" + UUID.randomUUID().toString().replace("-", "") + "_" + System.nanoTime();
        String hash = sha256(rawRefreshToken);

        UserSession session = UserSession.builder()
                .tenantId(tenantId)
                .userId(user.getId())
                .userEmail(user.getEmail())
                .refreshTokenHash(hash)
                .deviceName(deviceName != null ? deviceName : "Enterprise Web / Mobile Client")
                .ipAddress(ipAddress != null ? ipAddress : "127.0.0.1")
                .revoked(false)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(14, ChronoUnit.DAYS))
                .build();

        sessionRepository.save(session);
        return rawRefreshToken;
    }

    @Transactional
    public AuthResponse rotateRefreshToken(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token is required");
        }
        String hash = sha256(rawRefreshToken);
        UserSession existing = sessionRepository.findByRefreshTokenHashAndRevokedFalse(hash)
                .orElseThrow(() -> new BadCredentialsException("Invalid or revoked refresh session"));

        if (existing.getExpiresAt().isBefore(Instant.now())) {
            existing.setRevoked(true);
            existing.setRevokedAt(Instant.now());
            existing.setRevokeReason("EXPIRED");
            sessionRepository.save(existing);
            throw new BadCredentialsException("Refresh session expired");
        }

        // Rotate: revoke old session token immediately to detect replay attacks
        existing.setRevoked(true);
        existing.setRevokedAt(Instant.now());
        existing.setRevokeReason("ROTATED");
        sessionRepository.save(existing);

        User user = userRepository.findByEmailAndTenantId(existing.getUserEmail(), existing.getTenantId())
                .orElseThrow(() -> new BadCredentialsException("User not found for session"));

        String newAccessToken = jwtService.generateToken(user, existing.getTenantId());
        String newRefreshToken = issueRefreshSession(user, existing.getTenantId(), existing.getDeviceName(), existing.getIpAddress());

        Tenant tenant = tenantRepository.findByTenantIdentifier(existing.getTenantId())
                .orElse(Tenant.builder()
                        .tenantIdentifier(existing.getTenantId())
                        .schoolName("Apex Academy (Mumbai)")
                        .subdomain("apex")
                        .affiliationNumber("CBSE #113042")
                        .build());

        List<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .user(AuthResponse.UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .roles(roleNames)
                        .build())
                .tenant(AuthResponse.TenantDto.builder()
                        .tenantIdentifier(tenant.getTenantIdentifier())
                        .schoolName(tenant.getSchoolName())
                        .subdomain(tenant.getSubdomain())
                        .affiliationNumber(tenant.getAffiliationNumber())
                        .build())
                .build();
    }

    public List<UserSession> getActiveSessions(String userEmail) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        if (userEmail != null && !userEmail.isBlank()) {
            return sessionRepository.findByTenantIdAndUserEmailAndRevokedFalseOrderByCreatedAtDesc(tenantId, userEmail);
        }
        return sessionRepository.findByTenantIdAndRevokedFalseOrderByCreatedAtDesc(tenantId);
    }

    @Transactional
    public int revokeAllSessionsForUser(String userEmail, String reason) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<UserSession> active = sessionRepository.findByTenantIdAndUserEmailAndRevokedFalseOrderByCreatedAtDesc(tenantId, userEmail);
        Instant now = Instant.now();
        for (UserSession s : active) {
            s.setRevoked(true);
            s.setRevokedAt(now);
            s.setRevokeReason(reason != null ? reason : "ADMIN_EMERGENCY_REVOKE");
            sessionRepository.save(s);
        }

        auditLogService.recordEvent(
                "REVOKE_ALL_USER_SESSIONS",
                "identity.user_sessions",
                userEmail,
                String.format("Revoked %d active session(s) for %s. Reason: %s", active.size(), userEmail, reason)
        );
        return active.size();
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

