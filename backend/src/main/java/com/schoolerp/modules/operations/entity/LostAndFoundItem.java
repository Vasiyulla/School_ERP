package com.schoolerp.modules.operations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lost_and_found_items", schema = "operations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostAndFoundItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String trackingCode; // e.g. "LF-2026-0012"

    @Column(nullable = false)
    private String itemCategory; // UNIFORM_CLOTHING, ELECTRONICS_CALCULATOR, SPECTACLES, BOOKS_STATIONERY, BAGS_BOTTLES, VALUABLES_JEWELRY

    @Column(nullable = false, columnDefinition = "TEXT")
    private String itemDescription;

    @Column(nullable = false)
    private String foundLocation; // e.g. "Football Ground Bleachers", "Bus #04 Seat 12"

    @Column(nullable = false)
    private LocalDate foundDate;

    private String reportedByStaffName;

    private String photoUrl;

    @Column(nullable = false)
    @Builder.Default
    private String storageLockerRef = "Security Desk Locker A-1";

    @Column(nullable = false)
    @Builder.Default
    private String status = "FOUND_IN_CUSTODY"; // FOUND_IN_CUSTODY, CLAIMED_RETURNED, DONATED_AFTER_90_DAYS

    private UUID claimedByStudentId;

    private String claimedByName;

    private String claimVerificationNote;

    private LocalDateTime returnedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

