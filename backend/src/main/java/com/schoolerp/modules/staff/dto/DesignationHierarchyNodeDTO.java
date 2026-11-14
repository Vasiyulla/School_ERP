package com.schoolerp.modules.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignationHierarchyNodeDTO {
    private String designationCode;
    private String title;
    private String department;
    private int hierarchyTier;
    private String reportsTo;
    private String baseRole;
    private BigDecimal approvalLimitInr;
    private int activeEmployeeCount;

    @Builder.Default
    private List<DesignationHierarchyNodeDTO> subordinates = new ArrayList<>();
}

