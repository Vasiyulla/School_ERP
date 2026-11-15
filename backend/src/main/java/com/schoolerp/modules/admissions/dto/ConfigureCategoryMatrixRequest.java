package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigureCategoryMatrixRequest {
    @NotBlank
    private String gradeName; // e.g. "Grade 1"

    @NotBlank
    private String academicYear; // "2026-2027"

    @Min(1)
    private int totalSanctionedSeats;

    private int generalQuotaSeats;
    private int rteQuotaSeats;
    private int obcQuotaSeats;
    private int scQuotaSeats;
    private int stQuotaSeats;
    private int cwsnQuotaSeats;
    private int siblingQuotaSeats;
    private int staffWardQuotaSeats;
    private int singleGirlChildQuotaSeats;
    private int managementQuotaSeats;
}

