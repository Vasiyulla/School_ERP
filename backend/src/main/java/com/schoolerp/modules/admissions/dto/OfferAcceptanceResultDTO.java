package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferAcceptanceResultDTO {
    private UUID offerId;
    private String offerNumber;
    private String studentName;
    private String gradeOffered;
    private String offerStatus; // ACCEPTED
    private String parentSignatureName;
    private LocalDateTime acceptedAt;
    private BigDecimal seatLockDepositAmount;
    private String depositTransactionId;
    private String depositStatus; // PAID
    private String confirmationMessage;
}

