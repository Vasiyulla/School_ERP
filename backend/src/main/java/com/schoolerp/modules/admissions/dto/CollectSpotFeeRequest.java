package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectSpotFeeRequest {
    private BigDecimal amount;
    private String paymentMethod; // CASH, UPI_QR, CARD
    private String remarks;
}

