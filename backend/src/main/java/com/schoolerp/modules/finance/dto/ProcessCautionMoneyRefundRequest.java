package com.schoolerp.modules.finance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessCautionMoneyRefundRequest {

    @NotNull(message = "Student ID is mandatory")
    private UUID studentId;

    @Builder.Default
    private boolean autoDeductUnpaidTuitionDues = true;

    @Builder.Default
    private boolean autoDeductLabApparatusBreakages = true;

    @Builder.Default
    private String refundMode = "BANK_NEFT"; // BANK_NEFT, CHEQUE, UPI

    private String beneficiaryAccountNo;

    private String beneficiaryIfsc;

    private String reason;
}

