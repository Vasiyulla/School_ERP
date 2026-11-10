package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "syllabus_topic_progress", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusTopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String className; // e.g. Class 10 - Section A

    @Column(nullable = false)
    private String subject; // e.g. Mathematics

    @Column(nullable = false)
    private String teacherName;

    @Column(nullable = false)
    private String currentChapter; // e.g. Chapter 4: Quadratic Equations

    @Column(nullable = false)
    private String lastCompletedTopic; // e.g. Factorization & Discriminant Exercise 4.2

    @Column(nullable = false)
    private String nextTopicObjective; // e.g. Nature of Roots Exercise 4.3 + Worksheet #4

    private String resourceAttachmentUrl;

    @Builder.Default
    private double completionPercentage = 45.0;

    @Builder.Default
    private Instant updatedAt = Instant.now();
}

