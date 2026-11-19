package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "disciplinary_hearings", schema = "students", indexes = {
        @Index(name = "idx_disc_hearing_student", columnList = "tenant_id, student_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplinaryHearing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "docket_number", nullable = false, unique = true)
    private String docketNumber;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "infraction_category", nullable = false)
    private String infractionCategory; // BULLYING_HARASSMENT, ACADEMIC_DISHONESTY, VANDALISM, SUBSTANCE_VIOLATION, GROSS_INSUBORDINATION

    @Column(name = "charge_description", length = 2000, nullable = false)
    private String chargeDescription;

    @Column(name = "hearing_date", nullable = false)
    private LocalDate hearingDate;

    @Column(name = "committee_members", nullable = false)
    private String committeeMembers;

    @Column(name = "parent_notice_served_at")
    private Instant parentNoticeServedAt;

    @Column(name = "status", nullable = false)
    private String status; // SCHEDULED, HEARD, SANCTIONED, DISMISSED

    @Column(name = "findings_summary", length = 2000)
    private String findingsSummary;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
