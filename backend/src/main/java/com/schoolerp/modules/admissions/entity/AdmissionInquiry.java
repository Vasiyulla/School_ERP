package com.schoolerp.modules.admissions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admission_inquiries", schema = "admissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String inquiryNumber; // e.g., "INQ-2026-0042"

    private String tokenNumber; // e.g., "TOKEN-W14" for walk-in lounge token

    @Column(nullable = false)
    private String parentName;

    @Column(nullable = false)
    private String parentPhone;

    private String parentEmail;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String gradeApplyingFor; // e.g., "Grade 6", "Kindergarten"

    @Column(nullable = false)
    private String academicYear; // "2026-2027"

    @Column(nullable = false)
    private String pipelineStage; // INQUIRY_RECEIVED, CAMPUS_TOUR_BOOKED, APPLICATION_SUBMITTED, ENTRANCE_EVALUATION, PRINCIPAL_INTERVIEW, OFFER_ISSUED, ENROLLED, LOST_OR_DROPPED

    @Builder.Default
    private int leadScore = 20; // 0 - 100 conversion probability score

    @Column(nullable = false)
    private String sourceChannel; // WALK_IN, WEBSITE_FORM, WORD_OF_MOUTH, EDUCATION_FAIR, SOCIAL_MEDIA, ALUMNI_REFERRAL

    @Builder.Default
    private boolean hasEnrolledSibling = false;

    @Builder.Default
    private boolean proximityUnderFiveKm = false;

    @Builder.Default
    private String socialCategory = "GENERAL"; // GENERAL, OBC_NCL, SC, ST, EWS, CWSN_DIVYANG

    @Builder.Default
    private String admissionQuota = "OPEN_GENERAL"; // OPEN_GENERAL, RTE_25_PERCENT, SIBLING, ALUMNI, STAFF_WARD, SINGLE_GIRL_CHILD, DEFENSE_PARAMILITARY, MANAGEMENT

    private UUID siblingStudentId;

    private String assignedCounselorName;

    @Column(columnDefinition = "TEXT")
    private String counselorNotes;

    @Builder.Default
    private BigDecimal spotFeePaidAmount = BigDecimal.ZERO;

    private String spotFeeReceiptNumber;

    private String dropReason;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}

