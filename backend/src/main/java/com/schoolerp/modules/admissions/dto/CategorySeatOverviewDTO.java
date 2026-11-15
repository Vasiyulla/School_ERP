package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySeatOverviewDTO {
    private UUID id;
    private String gradeName;
    private String academicYear;
    private int totalSanctionedSeats;
    private int enrolledCount;
    private int activeOffersCount;
    private int availableSeatsTotal;
    
    // Category Breakdown: Sanctioned Seats
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

    // Real-Time Quota Waitlist Counts
    private Map<String, Integer> quotaWaitlistCounts;
}

