package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigureConcessionPolicyRequest {
    private String policyCode;
    private Integer siblingRank;
    private BigDecimal concessionPercentage;
    private String applicableFeeHead;
    private Boolean isActive;
}

