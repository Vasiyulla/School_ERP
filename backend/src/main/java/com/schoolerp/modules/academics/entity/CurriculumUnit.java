package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "curriculum_units", schema = "academics", indexes = {
        @Index(name = "idx_curriculum_grade_subject", columnList = "tenant_id, grade_level, subject_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "unit_code", nullable = false)
    private String unitCode;

    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "unit_title", nullable = false)
    private String unitTitle;

    @Column(name = "allocated_periods", nullable = false)
    private int allocatedPeriods;

    @Column(name = "completed_periods", nullable = false)
    private int completedPeriods;

    @Column(name = "learning_objectives", length = 2000)
    private String learningObjectives;

    @Column(name = "cbse_competencies", length = 1000)
    private String cbseCompetencies;

    @Column(name = "term", nullable = false)
    private String term; // Term 1, Term 2

    @Column(name = "status", nullable = false)
    private String status; // DRAFT, SUBMITTED_FOR_REVIEW, APPROVED, REVISION_REQUESTED

    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
