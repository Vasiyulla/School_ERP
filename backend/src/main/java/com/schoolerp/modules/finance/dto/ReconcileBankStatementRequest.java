package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReconcileBankStatementRequest {
    private String bankName; // e.g. HDFC_BANK
    private List<BankStatementEntryInputDTO> statementEntries;
}

