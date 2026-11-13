package com.schoolerp.modules.institution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "confidential_safety_incidents", schema = "institution")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfidentialSafetyIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String incidentRefCode; // e.g. "SAFE-2026-0001"

    @Column(nullable = false)
    private String classification; // POCSO_STATUTORY, POSH_STAFF, PEER_BULLYING_SEVERE, CYBER_HARASSMENT, SUBSTANCE_ABUSE

    @Column(nullable = false)
    @Builder.Default
    private String severityLevel = "HIGH_AMBER"; // CRITICAL_RED, HIGH_AMBER, MEDIUM_YELLOW

    private UUID involvedStudentOrStaffId;

    @Column(nullable = false)
    private String involvedPartyName;

    @Column(nullable = false)
    @Builder.Default
    private String roleInvolved = "STUDENT"; // STUDENT, STAFF, THIRD_PARTY

    @Column(columnDefinition = "TEXT", nullable = false)
    private String incidentSummary;

    @Column(columnDefinition = "TEXT")
    private String actionTakenInterim;

    @Builder.Default
    private boolean statutoryNotificationRequired = false;

    private String statutoryAuthorityRef; // e.g. "CWC-MUM-2026-89"

    private LocalDateTime statutoryNotificationDate;

    private String inquiryCommitteeMembers;

    @Column(nullable = false)
    @Builder.Default
    private String status = "OPEN_INQUIRY"; // OPEN_INQUIRY, INTERIM_PROTECTION, DISCIPLINARY_ACTION_TAKEN, CLOSED_RESOLVED

    private LocalDateTime closedAt;

    @Column(columnDefinition = "TEXT")
    private String closingRemarks;

    @Builder.Default
    private LocalDateTime reportedAt = LocalDateTime.now();
}

