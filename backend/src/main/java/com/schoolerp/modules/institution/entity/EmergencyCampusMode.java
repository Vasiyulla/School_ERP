package com.schoolerp.modules.institution.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emergency_campus_modes", schema = "institution")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCampusMode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private String operatingMode; // PHYSICAL_CAMPUS, VIRTUAL_EMERGENCY_CAMPUS, HYBRID_MODE, COMPLETE_EMERGENCY_SHUTDOWN

    @Column(nullable = false)
    private String emergencyReason; // HEAVY_RAINFALL_FLOOD, AIR_QUALITY_AQI_HAZARD, PANDEMIC_QUARANTINE, CYCLONE_STORM_ALERT, LOCAL_CIVIL_RESTRICTION, CATASTROPHIC_DISASTER

    private String declaredByStaffName;

    @Column(nullable = false)
    private LocalDate effectiveDate;

    private LocalDate endDate;

    @Builder.Default
    private boolean autoNotifyParents = true;

    @Builder.Default
    private boolean suspendTransportAlerts = true;

    @Builder.Default
    private boolean freezeLateFeesAndDeadlines = false; // Enabled for COMPLETE_EMERGENCY_SHUTDOWN

    private String govtOrderReference; // e.g. "DM/REV/DISASTER/2026/8912"

    @Column(columnDefinition = "TEXT")
    private String closureNoticeBulletin;

    @Builder.Default
    private int activeVirtualRoomsCount = 0;

    @Builder.Default
    private LocalDateTime declaredAt = LocalDateTime.now();
}

