package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "school_events", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category; // SPORTS, CULTURAL, ACADEMIC_OLYMPIAD, SCIENCE_EXHIBITION, CIVIC_NATIONAL_DAY, ANNUAL_DAY, LITERARY_DEBATE, COMMUNITY_OUTREACH

    @Column(nullable = false)
    private LocalDate eventDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private String venue; // e.g. Main Auditorium, Sports Ground, Science Lab 2

    private UUID coordinatorTeacherId;

    private String coordinatorTeacherName;

    @Column(nullable = false)
    @Builder.Default
    private String targetClasses = "ALL"; // e.g. "Class 9, Class 10" or "ALL"

    @Builder.Default
    private Integer maxParticipants = 100;

    @Builder.Default
    private BigDecimal budgetAllocated = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private String status = "PUBLISHED"; // DRAFT, PUBLISHED, REGISTRATION_OPEN, IN_PROGRESS, CONCLUDED, CANCELLED

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

