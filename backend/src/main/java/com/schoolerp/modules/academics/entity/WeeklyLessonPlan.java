package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "weekly_lesson_plans", schema = "academics", indexes = {
        @Index(name = "idx_lesson_plan_unit", columnList = "tenant_id, curriculum_unit_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyLessonPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "curriculum_unit_id", nullable = false)
    private UUID curriculumUnitId;

    @Column(name = "teacher_id")
    private UUID teacherId;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "week_number", nullable = false)
    private int weekNumber;

    @Column(name = "topics_planned", length = 2000, nullable = false)
    private String topicsPlanned;

    @Column(name = "teaching_pedagogy", length = 1000)
    private String teachingPedagogy;

    @Column(name = "assessment_method", length = 1000)
    private String assessmentMethod;

    @Column(name = "status", nullable = false)
    private String status; // DRAFT, SUBMITTED, APPROVED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
