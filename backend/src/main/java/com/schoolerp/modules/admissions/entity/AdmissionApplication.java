package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "admission_applications", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false, unique = true)
    private String applicationNumber; // e.g. APP-2026-0891

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String targetClass; // e.g. Class 11 - Science

    private String parentName;
    private String parentPhone;
    private String parentEmail;

    @Builder.Default
    private String status = "SUBMITTED"; // SUBMITTED, UNDER_REVIEW, INTERVIEW, APPROVED, ENROLLED

    private String documentStatus; // COMPLETE, PENDING_DOCS

    @Builder.Default
    private Instant appliedAt = Instant.now();
}

