package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "class_sections", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String className; // e.g. Class 10

    @Column(nullable = false)
    private String sectionName; // e.g. Section A

    private String classTeacherName; // e.g. Dr. Vikram Seth

    @Builder.Default
    private int capacity = 40;

    @Builder.Default
    private int enrolledCount = 38;
}

