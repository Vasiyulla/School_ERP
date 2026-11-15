package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionInquiryDTO {
    private UUID id;
    private String tenantId;
    private String inquiryNumber;
    private String tokenNumber;
    private String parentName;
    private String parentPhone;
    private String parentEmail;
    private String studentName;
    private String gradeApplyingFor;
    private String academicYear;
    private String pipelineStage;
    private int leadScore;
    private String sourceChannel;
    private boolean hasEnrolledSibling;
    private boolean proximityUnderFiveKm;
    private String socialCategory;
    private String admissionQuota;
    private String assignedCounselorName;
    private String counselorNotes;
    private BigDecimal spotFeePaidAmount;
    private String spotFeeReceiptNumber;
    private String dropReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

