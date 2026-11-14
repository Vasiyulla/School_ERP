package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDesignationRequest {
    private String designationCode;
    private String title;
    private String department;
    private Integer hierarchyTier; // 1 to 5
    private String reportsToDesignationCode;
    private String baseRole; // ROLE_ACCOUNTANT, ROLE_TEACHER, etc.
    private List<String> actionPermissions;
    private BigDecimal financialApprovalLimit;
    private String defaultCpcLevel; // LEVEL_10, LEVEL_8, LEVEL_7, LEVEL_6
}

