package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "academic_year_rollovers", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYearRollover {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String fromAcademicYear;

    @Column(nullable = false)
    private String toAcademicYear;

    @Builder.Default
    private int totalStudentsEvaluated = 0;

    @Builder.Default
    private int promotedCount = 0;

    @Builder.Default
    private int graduatedCount = 0;

    @Builder.Default
    private int retainedCount = 0;

    @Column(nullable = false)
    private String executedBy;

    @Builder.Default
    private Instant executedAt = Instant.now();

    @Builder.Default
    private String status = "COMPLETED";

    @Column(length = 2000)
    private String summaryNotes;
}

