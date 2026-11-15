package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admission_interview_slots", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionInterviewSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID inquiryId;

    @Column(nullable = false)
    private String slotType; // CAMPUS_TOUR, ENTRANCE_TEST, PRINCIPAL_INTERVIEW

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    private String interviewerName;

    private String venueOrRoom;

    @Builder.Default
    private String status = "SCHEDULED"; // SCHEDULED, COMPLETED, NO_SHOW, RESCHEDULED

    private Integer evaluationRating; // 1 to 5 stars

    @Column(columnDefinition = "TEXT")
    private String interviewerFeedback;

    private String recommendedGrade;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

