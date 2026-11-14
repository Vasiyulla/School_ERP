package com.schoolerp.modules.staff.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "teacher_cpd_records", schema = "staff")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCpdRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID staffId;

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private String trainingYear; // e.g. "2026-2027"

    @Column(nullable = false)
    private String agency; // CBSE_COE, DIKSHA, NISHTHA, SAHODAYA, IN_HOUSE

    @Column(nullable = false)
    private String trainingTopic;

    @Column(nullable = false)
    private String domain; // PEDAGOGY, INCLUSIVE_EDUCATION, CYBER_SAFETY, NEP_IMPLEMENTATION

    @Column(nullable = false)
    private Integer durationHours; // e.g. 5, 10, 15

    @Column(nullable = false)
    private LocalDate completionDate;

    private String certificateRef;
    private Double evaluationScore;
    private String verifiedBy;
}

