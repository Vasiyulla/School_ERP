package com.schoolerp.modules.communication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "grievance_tickets", schema = "communication")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrievanceTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String ticketNumber; // e.g. "GRV-2026-0042"

    private UUID parentUserId;

    @Column(nullable = false)
    private String parentName;

    private String parentPhone;

    private String studentName;

    private String className;

    @Column(nullable = false)
    private String category; // ACADEMICS_GRADING, TRANSPORT_BUS, CANTEEN_FOOD, FACULTY_BEHAVIOR, FEE_ACCOUNTS, INFRASTRUCTURE

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private String currentTier = "TIER_1_TEACHER"; // TIER_1_TEACHER, TIER_2_HOD, TIER_3_PRINCIPAL_DESK

    @Column(nullable = false)
    @Builder.Default
    private String status = "OPEN"; // OPEN, IN_PROGRESS, ESCALATED_TO_PRINCIPAL, RESOLVED, CLOSED

    @Column(nullable = false)
    private LocalDateTime slaDeadline; // typically createdAt + 48 hours

    @Builder.Default
    private boolean isSlaBreached = false;

    @Column(columnDefinition = "TEXT")
    private String resolutionNotes;

    private String resolvedByStaffName;

    private LocalDateTime resolvedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

