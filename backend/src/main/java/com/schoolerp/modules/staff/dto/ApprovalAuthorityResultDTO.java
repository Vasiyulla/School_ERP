package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalAuthorityResultDTO {
    private String employeeCode;
    private String employeeName;
    private String designationCode;
    private String designationTitle;
    private BigDecimal requestedAmount;
    private BigDecimal approvalLimitInr;
    private String status; // AUTHORIZED, ESCALATION_REQUIRED
    private String escalateToDesignation;
    private String message;
}

