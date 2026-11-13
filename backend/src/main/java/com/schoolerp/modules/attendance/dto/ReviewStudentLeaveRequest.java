package com.schoolerp.modules.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStudentLeaveRequest {

    @NotBlank(message = "Decision status is required (APPROVED or REJECTED)")
    private String status; // APPROVED, REJECTED

    private String reviewRemarks;
}

