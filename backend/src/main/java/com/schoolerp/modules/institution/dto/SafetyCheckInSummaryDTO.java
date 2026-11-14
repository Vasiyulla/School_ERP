package com.schoolerp.modules.institution.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafetyCheckInSummaryDTO {
    private String tenantId;
    private int totalCheckIns;
    private int safeAtHomeCount;
    private int evacuatedToShelterCount;
    private int needUrgentAssistanceCount;
    private int medicalEmergencyCount;
    private List<EmergencySafetyCheckInDTO> urgentCases;
}

