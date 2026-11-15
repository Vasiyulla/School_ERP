package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionsFunnelAnalyticsDTO {
    private long totalInquiries;
    private Map<String, Long> stageCounts;
    private long walkInCount;
    private long websiteCount;
    private BigDecimal spotFeesCollectedTotal;
    private long offersIssuedCount;
    private long studentsEnrolledCount;
    private double conversionRatePercent;
}

