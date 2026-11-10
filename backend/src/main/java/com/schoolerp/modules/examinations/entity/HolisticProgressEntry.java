package com.schoolerp.modules.examinations.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "holistic_progress_entries", schema = "examinations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolisticProgressEntry {

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
    @Builder.Default
    private String termName = "Term 1 AY 2026-27";

    @Column(nullable = false)
    private String evaluatorType; // SELF, PEER, TEACHER

    @Column(nullable = false)
    private String competencyDomain; // COGNITIVE_INQUIRY, SOCIO_EMOTIONAL_EMPATHY, VOCATIONAL_ROBOTICS, LEADERSHIP_TEAMWORK

    @Column(nullable = false)
    private String proficiencyLevel; // BEGINNER, PROGRESSING, PROFICIENT, EXEMPLARY

    private int numericScore; // 1 = Beginner, 2 = Progressing, 3 = Proficient, 4 = Exemplary

    @Builder.Default
    private double experientialHoursLogged = 0.0;

    @Column(length = 1000)
    private String evidenceNotes;

    @Builder.Default
    private Instant recordedAt = Instant.now();
}

