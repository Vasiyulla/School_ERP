package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmitCardDTO {
    private String admitCardToken; // e.g. "ADMIT-2026-XXXX"
    private String inquiryNumber;
    private String studentName;
    private String parentName;
    private String gradeApplyingFor;
    private String academicYear;
    private String testCode;
    private LocalDateTime testDate;
    private String examHall;
    private String reportingTime;
    private String testInstructions;
    private String digitalVerificationHash;
}

