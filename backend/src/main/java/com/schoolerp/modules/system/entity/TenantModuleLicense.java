package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenant_module_licenses", schema = "system")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantModuleLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String moduleKey; // FINANCE, EXAMINATIONS, ATTENDANCE, FLEET_GPS, LIBRARY, ADMISSIONS, COMMUNICATION

    @Column(nullable = false)
    private String moduleName;

    @Column(nullable = false)
    @Builder.Default
    private String tier = "ENTERPRISE"; // STARTER, PROFESSIONAL, ENTERPRISE

    @Column(nullable = false)
    @Builder.Default
    private boolean isEnabled = true;

    private Instant expiresAt;

    private String updatedBy;

    @Builder.Default
    private Instant updatedAt = Instant.now();
}

