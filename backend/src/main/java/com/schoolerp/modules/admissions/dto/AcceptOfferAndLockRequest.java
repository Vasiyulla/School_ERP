package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptOfferAndLockRequest {
    @NotBlank
    private String parentSignatureName; // e.g. "Vikram Sharma"

    private String parentIpAddress; // e.g. "103.21.244.18"

    private BigDecimal seatLockDepositAmount; // e.g. ₹25,000

    private String depositPaymentMethod; // UPI, NET_BANKING, DEBIT_CARD
}

