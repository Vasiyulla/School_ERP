package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "admission_queue_tokens", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionQueueToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String tokenNumber; // e.g., TKN-ENQ-101, TKN-DOC-102, TKN-FEE-103

    @Column(nullable = false)
    private String category; // ENQUIRY, DOCUMENT_VERIFICATION, FEE_PAYMENT, PRINCIPAL_INTERVIEW

    @Column(nullable = false)
    private String visitorName;

    @Column(nullable = false)
    private String visitorPhone;

    private String studentName;

    private String gradeApplying;

    @Column(nullable = false)
    @Builder.Default
    private String status = "WAITING"; // WAITING, CALLED, SERVING, COMPLETED, NO_SHOW, CANCELLED

    private String assignedCounter; // e.g., Counter 1, Counter 2

    private UUID assignedOfficerId;

    @Builder.Default
    private Integer waitingMinutes = 0;

    @Builder.Default
    private Integer serviceMinutes = 0;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private Instant calledAt;

    private Instant completedAt;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private Instant updatedAt = Instant.now();
}
