package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementEntryInputDTO {
    private String utrNumber;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private String narration;
}

