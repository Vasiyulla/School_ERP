package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hostel_exeat_passes", schema = "operations",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_exeat_number", columnNames = {"tenantId", "passNumber"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelExeatPass {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String passNumber; // e.g. "EXEAT-2026-0042"

    @Column(nullable = false)
    private String exeatType; // WEEKEND_HOME, MEDICAL_TOWN, EMERGENCY_OUTSTATION

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime expectedReturnTime;

    private LocalDateTime actualReturnTime;

    @Builder.Default
    private boolean parentAuthorized = true;

    private String parentContactPhone;

    @Builder.Default
    private String status = "APPROVED"; // APPROVED, DEPARTED, RETURNED, OVERDUE

    private LocalDateTime gateDepartureScannedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

