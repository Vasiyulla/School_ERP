package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "student_risk_assessments", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    private int compositeRiskScore; // 0 - 100

    @Column(nullable = false)
    private String riskTier; // NORMAL, WATCHLIST, CRITICAL_INTERVENTION

    private double attendanceSignal;
    private double academicAverageSignal;
    private int recentInfirmaryVisits;
    private int netBehaviorScore;

    @Column(length = 1000)
    private String primaryRiskDrivers;

    @Column(length = 1000)
    private String recommendedIntervention;

    @Builder.Default
    private Instant evaluatedAt = Instant.now();
}

