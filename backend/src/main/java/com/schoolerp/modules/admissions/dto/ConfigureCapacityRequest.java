package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigureCapacityRequest {
    private String academicYear;
    private String gradeName;
    private int totalSanctionedSeats;
    private int generalQuotaSeats;
    private int rteQuotaSeats;
    private int siblingStaffQuotaSeats;
    private int managementQuotaSeats;
}

