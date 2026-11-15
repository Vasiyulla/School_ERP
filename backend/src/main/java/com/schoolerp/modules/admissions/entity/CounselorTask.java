package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "counselor_tasks", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Column(nullable = false)
    private String counselorName;

    @Column(nullable = false)
    private String taskType; // VERIFICATION_CALL, TOUR_REMINDER, MISSING_DOC_CHASER, OFFER_FOLLOWUP

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Builder.Default
    private String priority = "NORMAL"; // URGENT, HIGH, NORMAL

    @Builder.Default
    private String status = "PENDING"; // PENDING, COMPLETED, CANCELLED

    @Column(columnDefinition = "TEXT")
    private String outcomeNotes;

    private LocalDateTime completedAt;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

