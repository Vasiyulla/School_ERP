package com.schoolerp.modules.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_mfa_profiles", schema = "identity", indexes = {
        @Index(name = "idx_user_mfa_user_tenant", columnList = "user_id, tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMfaProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "secret_key", nullable = false)
    private String secretKey;

    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;

    @Column(name = "backup_scratch_codes", length = 1000)
    private String backupScratchCodes; // Comma-separated single-use recovery codes

    @Column(name = "enabled_at")
    private Instant enabledAt;

    @Column(name = "last_verified_at")
    private Instant lastVerifiedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
