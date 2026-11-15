package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInquiryRequest {
    private String parentName;
    private String parentPhone;
    private String parentEmail;
    private String studentName;
    private String gradeApplyingFor;
    private String academicYear;
    private String sourceChannel;
    private boolean hasEnrolledSibling;
    private boolean proximityUnderFiveKm;
    private String assignedCounselorName;
    private String counselorNotes;
}

