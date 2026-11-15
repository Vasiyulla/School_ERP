package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatutoryCertificateRequest {
    @NotNull
    private UUID inquiryId;

    @NotBlank
    private String certificateType; // CASTE_SC, CASTE_ST, CASTE_OBC_NCL, EWS_INCOME, CWSN_DISABILITY, SINGLE_GIRL_AFFIDAVIT, RESIDENCE_PROOF

    @NotBlank
    private String certificateNumber;

    @NotBlank
    private String issuingAuthority; // e.g. "Tehsildar / SDM"

    private LocalDate issueDate;
    private String validFinancialYear; // e.g. "2026-2027"
    private String verificationRemarks;
    private boolean markVerifiedImmediately;
}

