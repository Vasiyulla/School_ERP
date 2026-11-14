package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CautionMoneyRefundResultDTO {

    private String tenantId;
    private String refundVoucherNumber;
    private String studentName;
    private String admissionNumber;
    private String className;
    private BigDecimal initialDepositAmount;
    private BigDecimal unpaidFeeDuesDeduction;
    private BigDecimal labBreakageDeduction;
    private BigDecimal netRefundPayoutInr;
    private String refundStatus; // REFUNDED_CLOSED, ZERO_BALANCE_ADJUSTED
    private String paymentReference;
    private Instant processedAt;
    private String ledgerJournalEntryId;
}

