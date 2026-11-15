package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueOfferRequest {
    private UUID inquiryId;
    private BigDecimal tuitionFeeTotal;
    private BigDecimal scholarshipOrConcessionAmount;
    private LocalDate validUntilDate;
}

