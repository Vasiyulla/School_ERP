package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "teacher_substitutions", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSubstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private LocalDate substitutionDate;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private int periodNumber;

    @Column(nullable = false)
    private String timeSlot;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private String subject;

    private String roomNumber;

    @Column(nullable = false)
    private String absentTeacherName;

    private UUID absentTeacherId;

    @Column(nullable = false)
    private String substituteTeacherName;

    private UUID substituteTeacherId;

    @Builder.Default
    private String status = "ASSIGNED"; // ASSIGNED, CONFIRMED, COMPLETED

    private String assignedBy;

    @Builder.Default
    private Instant assignedAt = Instant.now();
}

