package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProspectusInventorySummaryDTO {
    private long totalKits;
    private long totalInStock;
    private long totalSold;
    private long totalComplimentaryOrDamaged;
    private BigDecimal totalRevenueCollected;
}
