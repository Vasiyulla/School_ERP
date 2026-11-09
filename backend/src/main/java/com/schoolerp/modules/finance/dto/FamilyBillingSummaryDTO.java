package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyBillingSummaryDTO {
    private String guardianName;
    private String guardianEmail;
    private String guardianPhone;

    private List<ChildFeeLineDTO> childrenLines;

    private BigDecimal totalGrossFee;
    private BigDecimal totalSiblingConcessions;
    private BigDecimal totalNetPayable;

    private String unifiedOrderId;
    private String currency; // INR
    private String paymentGateway; // RAZORPAY

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChildFeeLineDTO {
        private UUID studentId;
        private String studentName;
        private String admissionNumber;
        private String className;
        private String sectionName;
        private String invoiceNumber;
        private LocalDate dueDate;
        private BigDecimal grossAmount;
        private String concessionType; // NONE, SIBLING_DISCOUNT
        private BigDecimal concessionAmount;
        private BigDecimal netAmount;
        private String invoiceStatus;
    }
}

