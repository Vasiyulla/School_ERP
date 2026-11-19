package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "disciplinary_sanctions", schema = "students", indexes = {
        @Index(name = "idx_disc_sanction_student", columnList = "tenant_id, student_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplinarySanction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "hearing_docket_id", nullable = false)
    private UUID hearingDocketId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "sanction_type", nullable = false)
    private String sanctionType; // IN_SCHOOL_SUSPENSION, OUT_OF_SCHOOL_SUSPENSION, BUS_PRIVILEGE_REVOKED, GOOD_BEHAVIOR_BOND, COMMUNITY_SERVICE

    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "sanction_terms", length = 2000, nullable = false)
    private String sanctionTerms;

    @Column(name = "reinstatement_conditions", length = 2000)
    private String reinstatementConditions;

    @Column(name = "authorized_by", nullable = false)
    private String authorizedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
