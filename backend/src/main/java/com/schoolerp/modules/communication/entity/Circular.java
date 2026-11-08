package com.schoolerp.modules.communication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "circulars", schema = "communication")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Circular {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000, nullable = false)
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private String targetAudience = "ALL"; // ALL, PARENTS, TEACHERS, GRADE_10, STAFF

    private String targetGradeLevel; // e.g. Class 10

    @Column(nullable = false)
    @Builder.Default
    private String priority = "NORMAL"; // NORMAL, IMPORTANT, URGENT

    private String attachmentUrl; // e.g. s3://school-erp-docs/circulars/annual-sports-2026.pdf

    @Column(nullable = false)
    private String publishedBy;

    @Builder.Default
    private LocalDate publishedDate = LocalDate.now();

    @Builder.Default
    private String status = "PUBLISHED"; // DRAFT, PUBLISHED, ARCHIVED

    @Builder.Default
    private Instant createdAt = Instant.now();
}

