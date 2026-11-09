package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "homework_assignments", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String className; // Class 10

    @Column(nullable = false)
    private String sectionName; // Section A

    @Column(nullable = false)
    private String subject; // Mathematics, Physics

    @Column(nullable = false)
    private String title; // Quadratic Equations & Parabola Applications

    @Column(columnDefinition = "TEXT")
    private String instructions; // Complete Exercise 4.2 Questions 1-15 in homework notebook

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private LocalDate assignedDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Builder.Default
    private double maxMarks = 20.0;

    private String attachmentUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}

