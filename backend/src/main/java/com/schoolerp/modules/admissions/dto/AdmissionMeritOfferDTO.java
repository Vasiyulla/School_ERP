package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionMeritOfferDTO {
    private UUID id;
    private String tenantId;
    private UUID inquiryId;
    private String offerNumber;
    private String studentName;
    private String gradeOffered;
    private String academicYear;
    private BigDecimal tuitionFeeTotal;
    private BigDecimal scholarshipOrConcessionAmount;
    private BigDecimal netAdmissionPayable;
    private LocalDate validUntilDate;
    private String offerStatus;
    private UUID convertedStudentId;
    private UUID convertedInvoiceId;
    private LocalDateTime issuedAt;
    private LocalDateTime acceptedAt;
}

