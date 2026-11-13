package com.schoolerp.modules.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyStudentLeaveRequest {

    @NotNull(message = "Student ID is required")
    private UUID studentId;

    @NotBlank(message = "Leave type is required (e.g. SICK_LEAVE, CASUAL, FAMILY_EMERGENCY, SPORTS_DUTY)")
    private String leaveType;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String medicalCertificateUrl;
}

