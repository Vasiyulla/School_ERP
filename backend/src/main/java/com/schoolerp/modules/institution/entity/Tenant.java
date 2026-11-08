package com.schoolerp.modules.institution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tenants", schema = "institution")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String tenantIdentifier; // e.g. tenant_apex_mumbai_01

    @Column(nullable = false)
    private String schoolName; // e.g. Apex Academy Mumbai

    @Column(unique = true)
    private String subdomain; // e.g. apex

    private String affiliationNumber; // CBSE #113042

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, SUSPENDED, TRIAL

    @Builder.Default
    private String subscriptionTier = "ENTERPRISE"; // STARTER, STANDARD, ENTERPRISE

    @Builder.Default
    private int studentCapacity = 20000;

    @Builder.Default
    private Instant createdAt = Instant.now();
}

