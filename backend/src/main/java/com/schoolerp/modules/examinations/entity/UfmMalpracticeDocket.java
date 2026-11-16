package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ufm_malpractice_dockets", schema = "examinations",
       uniqueConstraints = @UniqueConstraint(name = "uq_tenant_ufm_docket", columnNames = {"tenantId", "docketNumber"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UfmMalpracticeDocket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String docketNumber; // e.g. "UFM-2026-0012"

    @Column(nullable = false)
    private UUID examScheduleId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private LocalDateTime incidentTimestamp;

    @Column(nullable = false)
    private String contrabandType; // UNAUTHORIZED_NOTES, MOBILE_DEVICE, SMART_WATCH, IMPERSONATION

    @Column(nullable = false, columnDefinition = "TEXT")
    private String invigilatorRemarks;

    @Column(columnDefinition = "TEXT")
    private String studentStatement;

    @Builder.Default
    private String verdictStatus = "UNDER_COMMITTEE_REVIEW"; // UNDER_COMMITTEE_REVIEW, WARNING_ISSUED, PAPER_CANCELLED, EXPELLED

    private LocalDate verdictDate;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
