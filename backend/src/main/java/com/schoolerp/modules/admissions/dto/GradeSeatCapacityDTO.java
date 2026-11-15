package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeSeatCapacityDTO {
    private UUID id;
    private String tenantId;
    private String academicYear;
    private String gradeName;
    private int totalSanctionedSeats;
    private int generalQuotaSeats;
    private int rteQuotaSeats;
    private int siblingStaffQuotaSeats;
    private int managementQuotaSeats;
    private int enrolledCount;
    private int activeOffersCount;
    private int availableSeats;
    private int waitlistCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

