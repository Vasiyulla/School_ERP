package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class FinanceGovernanceDTOs {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitConcessionRequest {
        private UUID studentId;
        private String studentName;
        private String academicYear;
        private String reasonCategory;
        private BigDecimal requestedAmount;
        private String makerUserId;
        private String justification;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorizeConcessionRequest {
        private boolean approved;
        private BigDecimal approvedAmount;
        private String remarks;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepositPdcRequest {
        private UUID studentId;
        private String studentName;
        private String chequeNumber;
        private String drawerBank;
        private String drawerAccountName;
        private BigDecimal amount;
        private LocalDate maturityDate;
        private String vaultLockerLocation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PdcMaturityScheduleDTO {
        private int totalCheques;
        private BigDecimal totalAmount;
        private int maturingNext7DaysCount;
        private int maturingNext30DaysCount;
        private List<PdcSummaryDTO> cheques;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PdcSummaryDTO {
        private UUID id;
        private String chequeNumber;
        private String drawerBank;
        private String studentName;
        private BigDecimal amount;
        private LocalDate maturityDate;
        private String vaultLockerLocation;
        private String status;
        private int daysToMaturity;
    }
}
