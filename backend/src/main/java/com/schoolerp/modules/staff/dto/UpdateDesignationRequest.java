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
public class UpdateDesignationRequest {
    private String title;
    private String department;
    private Integer hierarchyTier;
    private String reportsToDesignationCode;
    private String baseRole;
    private List<String> actionPermissions;
    private BigDecimal financialApprovalLimit;
    private String defaultCpcLevel;
    private Boolean isActive;
}

