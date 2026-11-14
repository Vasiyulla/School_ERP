package com.schoolerp.modules.institution.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeclareEmergencyModeRequest {
    private String operatingMode; // PHYSICAL_CAMPUS, VIRTUAL_EMERGENCY_CAMPUS, HYBRID_MODE, COMPLETE_EMERGENCY_SHUTDOWN
    private String emergencyReason; // HEAVY_RAINFALL_FLOOD, AIR_QUALITY_AQI_HAZARD, PANDEMIC_QUARANTINE, CYCLONE_STORM_ALERT, LOCAL_CIVIL_RESTRICTION, CATASTROPHIC_DISASTER
    private LocalDate effectiveDate;
    private LocalDate endDate;
    private String govtOrderReference;
    private String closureNoticeBulletin;
    private Boolean autoNotifyParents;
    private Boolean suspendTransportAlerts;
    private Boolean freezeLateFeesAndDeadlines;
}

