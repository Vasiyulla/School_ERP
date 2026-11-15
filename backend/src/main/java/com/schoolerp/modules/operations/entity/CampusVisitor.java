package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "campus_visitors", schema = "operations",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_visitor_pass", columnNames = {"tenantId", "visitorPassNumber"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampusVisitor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String visitorPassNumber; // e.g. "VIS-2026-0891"

    @Column(nullable = false)
    private String visitorName;

    @Column(nullable = false)
    private String visitorPhone;

    @Column(nullable = false)
    private String idProofType; // AADHAAR, DRIVING_LICENSE, VOTER_ID, PASSPORT

    @Column(nullable = false)
    private String idProofNumber;

    @Column(nullable = false)
    private String purposeOfVisit; // ADMISSION_ENQUIRY, PARENT_MEETING, VENDOR_DELIVERY, GOVT_INSPECTION

    private UUID hostStaffId;

    @Column(nullable = false)
    private String hostStaffName;

    @Builder.Default
    private String hostApprovalStatus = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column(nullable = false)
    private String entryGate; // Gate 1 - Main Arch, Gate 2 - Sports Complex

    @Column(nullable = false)
    private String badgeQrToken; // QR token for badge scan

    @Builder.Default
    private LocalDateTime checkedInAt = LocalDateTime.now();

    private LocalDateTime checkedOutAt;

    @Builder.Default
    private String status = "ACTIVE_ON_CAMPUS"; // ACTIVE_ON_CAMPUS, CHECKED_OUT, OVERSTAYED, ENTRY_DENIED

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

