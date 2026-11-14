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
public class CheckApprovalAuthorityRequest {
    private String employeeCode;
    private String actionType; // FEE_REFUND, VENDOR_PO, STAFF_LOAN
    private BigDecimal amount;
}

