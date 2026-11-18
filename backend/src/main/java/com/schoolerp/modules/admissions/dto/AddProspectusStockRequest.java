package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProspectusStockRequest {

    @NotBlank(message = "Academic year is required (e.g. 2026-2027)")
    private String academicYear;

    @NotNull(message = "Price per kit is required")
    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "List of serial barcodes is required")
    private List<String> serialBarcodes; // e.g. ["KIT-2026-0001", "KIT-2026-0002", ...]
}
