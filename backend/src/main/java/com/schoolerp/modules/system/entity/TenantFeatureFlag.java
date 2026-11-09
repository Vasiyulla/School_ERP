package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenant_feature_flags", schema = "system", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenantId", "featureKey"})
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantFeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String featureKey;

    @Column(nullable = false)
    private String featureName;

    @Column(nullable = false)
    private String category; // FINANCE, ATTENDANCE, EXAMINATIONS, OPERATIONS, STAFF, COMMUNICATION

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private String tier = "STANDARD"; // STARTER, GROWTH, ENTERPRISE

    @Builder.Default
    private Instant updatedAt = Instant.now();
}

