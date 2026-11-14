package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pbl_projects", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PblProject {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String gradeLevel;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private int totalMilestones = 4; // e.g., 1: Proposal, 2: Research Draft, 3: Prototype, 4: Final Presentation & Viva

    @Column(columnDefinition = "TEXT")
    private String scoringRubric;

    private String coordinatorTeacherName;

    private LocalDate dueDate;

    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, COMPLETED, ARCHIVED

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

