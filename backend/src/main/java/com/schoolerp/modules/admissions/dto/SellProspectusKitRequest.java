package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellProspectusKitRequest {

    @NotBlank(message = "Serial barcode is required")
    private String serialBarcode;

    @NotBlank(message = "Buyer parent name is required")
    private String buyerParentName;

    @NotBlank(message = "Buyer phone is required")
    private String buyerPhone;

    @NotBlank(message = "Payment mode is required (CASH, UPI_QR, CARD, WAIVED)")
    private String paymentMode;

    private String transactionRef;

    private UUID soldByUserId;

    private UUID linkedInquiryId;
}
