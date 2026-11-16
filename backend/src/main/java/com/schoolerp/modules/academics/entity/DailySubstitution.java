package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "daily_substitutions", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySubstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private LocalDate substitutionDate;

    @Column(nullable = false)
    private int periodNumber;

    @Column(nullable = false)
    private UUID classroomId;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private UUID absentTeacherId;

    @Column(nullable = false)
    private String absentTeacherName;

    @Column(nullable = false)
    private UUID substituteTeacherId;

    @Column(nullable = false)
    private String substituteTeacherName;

    @Column(nullable = false)
    private String subjectName;

    @Column(columnDefinition = "TEXT")
    private String lessonHandoffBrief;

    @Builder.Default
    private String status = "ASSIGNED"; // ASSIGNED, NOTIFIED, COMPLETED, CANCELLED

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
