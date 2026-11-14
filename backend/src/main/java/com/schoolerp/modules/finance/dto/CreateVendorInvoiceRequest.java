package com.schoolerp.modules.finance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateVendorInvoiceRequest {

    @NotBlank(message = "Vendor name is mandatory")
    private String vendorName;

    @NotBlank(message = "Vendor GSTIN is mandatory")
    @Builder.Default
    private String vendorGstin = "27AAACG1234F1Z5";

    @NotBlank(message = "Vendor PAN is mandatory")
    @Builder.Default
    private String vendorPan = "AAACG1234F";

    @NotBlank(message = "Service category is mandatory")
    @Builder.Default
    private String serviceCategory = "FLEET_TRANSPORT";

    @NotBlank(message = "Invoice number is mandatory")
    private String invoiceNumber;

    @Builder.Default
    private LocalDate invoiceDate = LocalDate.now();

    @NotBlank(message = "TDS section is mandatory (SECTION_194C_CONTRACTORS or SECTION_194J_PROFESSIONAL)")
    @Builder.Default
    private String tdsSection = "SECTION_194C_CONTRACTORS";

    @Builder.Default
    private double tdsRatePercentage = 2.0; // 1% for individual/HUF, 2% for company, 10% for professional

    @NotNull(message = "Gross amount is mandatory")
    @DecimalMin(value = "1.00", message = "Invoice amount must be positive")
    private BigDecimal grossAmount;

    private String purchaseOrderRef;

    private String remarks;
}

