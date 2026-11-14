package com.schoolerp.modules.attendance.dto;

import com.schoolerp.modules.attendance.entity.BiometricPunchLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricSyncResultDTO {
    private String tenantId;
    private String deviceSerialNumber;
    private int totalPunchesReceived;
    private int presentCount;
    private int lateArrivalCount;
    private int earlyDepartureCount;
    private List<BiometricPunchLog> processedLogs;
}

