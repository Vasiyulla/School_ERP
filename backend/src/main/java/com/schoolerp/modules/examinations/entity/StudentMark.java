package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "marks", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentMark {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String termName; // e.g. Term 1 Mid-Term Examination

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String subject; // e.g. Mathematics

    private int theoryScore; // Max 80
    private int practicalScore; // Max 20
    private int internalScore; // Max 10
    private int totalScore; // Normalized 100%
    private String gradeBand; // A1, A2, B1, B2, C1, C2, D, E
    private String remarks;

    @Builder.Default
    private String status = "DRAFT"; // DRAFT, LOCKED
}

