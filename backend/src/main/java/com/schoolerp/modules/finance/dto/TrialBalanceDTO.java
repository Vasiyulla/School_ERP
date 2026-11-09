package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrialBalanceDTO {
    private String tenantId;
    private Instant generatedAt;
    private List<AccountBalanceDTO> accounts;
    private BigDecimal totalDebitSum;
    private BigDecimal totalCreditSum;
    private boolean isBalanced; // sum(Debits) == sum(Credits)

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBalanceDTO {
        private String accountCode; // e.g. 1020-BANK-HDFC, 4010-TUITION-FEE, 5010-STAFF-SALARIES
        private String accountName;
        private String accountCategory; // ASSET, LIABILITY, REVENUE, EXPENSE
        private BigDecimal totalDebit;
        private BigDecimal totalCredit;
        private BigDecimal netBalance;
    }
}

