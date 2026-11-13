package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "apparatus_breakages", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApparatusBreakage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID labItemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String labName;

    @Column(nullable = false)
    private UUID studentId;

    private String studentAdmissionNo;

    @Column(nullable = false)
    private String studentName;

    private String className;

    @Column(nullable = false)
    private LocalDate incidentDate;

    @Column(nullable = false)
    @Builder.Default
    private String faultType = "ACCIDENTAL"; // ACCIDENTAL, NEGLIGENCE

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal assessedDamageCost;

    @Column(nullable = false)
    @Builder.Default
    private String fineStatus = "INVOICED_TO_STUDENT"; // ASSESSED, INVOICED_TO_STUDENT, PAID, WAIVED

    private String invoiceNumberGenerated; // e.g. "INV-BRK-2026-X891"

    private String labTechnicianStaffName;

    private String remarks;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

