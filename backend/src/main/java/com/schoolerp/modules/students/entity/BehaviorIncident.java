package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "behavior_incidents", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorIncident {

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

    @Column(nullable = false)
    private String houseName;

    @Column(nullable = false)
    private String incidentType; // MERIT (Positive points), DEMERIT (Negative points)

    @Column(nullable = false)
    private String category; // ACADEMIC_EXCELLENCE, SPORTSMANSHIP, LEADERSHIP, LAB_SAFETY, TARDINESS, UNIFORM_VIOLATION, DISCIPLINARY

    @Column(nullable = false)
    private int points; // e.g. +10, -5

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String reportedByStaffName;

    @Column(nullable = false)
    private LocalDate incidentDate;

    @Builder.Default
    private boolean parentNotified = false;

    private LocalDateTime recordedAt;
}

