package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alumni_profiles", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String admissionNumber;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private int graduationYear; // e.g. 2025, 2026

    private double class12BoardScorePercentage;

    private String email;

    private String phone;

    @Column(nullable = false)
    @Builder.Default
    private String currentStatus = "HIGHER_EDUCATION"; // HIGHER_EDUCATION, EMPLOYED, ENTREPRENEUR

    private String linkedinUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

