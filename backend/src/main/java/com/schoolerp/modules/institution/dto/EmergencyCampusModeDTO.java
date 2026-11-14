package com.schoolerp.modules.institution.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCampusModeDTO {
    private UUID id;
    private String operatingMode;
    private String emergencyReason;
    private String declaredByStaffName;
    private LocalDate effectiveDate;
    private LocalDate endDate;
    private boolean autoNotifyParents;
    private boolean suspendTransportAlerts;
    private boolean freezeLateFeesAndDeadlines;
    private String govtOrderReference;
    private String closureNoticeBulletin;
    private int activeVirtualRoomsCount;
    private LocalDateTime declaredAt;
}

