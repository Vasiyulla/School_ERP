package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "faculty_appraisals", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyAppraisal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String academicYear; // e.g. "2026-2027"

    @Column(nullable = false)
    private UUID teacherId;

    @Column(nullable = false)
    private String teacherName;

    private String department;

    private String designation;

    @Column(nullable = false)
    @Builder.Default
    private double selfAppraisalScore = 20.0; // max 25

    @Column(nullable = false)
    @Builder.Default
    private double academicResultsScore = 30.0; // max 35

    @Column(nullable = false)
    @Builder.Default
    private double institutionalDutyScore = 18.0; // max 20

    @Column(nullable = false)
    @Builder.Default
    private double principalObservationScore = 18.0; // max 20

    @Column(nullable = false)
    private double compositeScore; // 0 - 100

    @Column(nullable = false)
    private String appraisalBand; // EXEMPLARY_A_PLUS, COMMENDABLE_A, SATISFACTORY_B, NEEDS_IMPROVEMENT_C

    @Column(nullable = false)
    private double recommendedIncrementPercentage; // e.g. 10.0, 7.5, 5.0, 0.0

    @Column(columnDefinition = "TEXT")
    private String principalEvaluationNotes;

    private String evaluatedByStaffName;

    @Builder.Default
    private LocalDateTime evaluatedAt = LocalDateTime.now();
}

