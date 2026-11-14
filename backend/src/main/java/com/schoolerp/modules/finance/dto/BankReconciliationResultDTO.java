package com.schoolerp.modules.finance.dto;

import com.schoolerp.modules.finance.entity.BankStatementEntry;
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
public class BankReconciliationResultDTO {
    private String tenantId;
    private String bankName;
    private int totalProcessed;
    private int autoReconciledCount;
    private int suspensePoolCount;
    private BigDecimal totalReconciledAmount;
    private List<BankStatementEntry> reconciledEntries;
    private List<BankStatementEntry> suspenseEntries;
}

