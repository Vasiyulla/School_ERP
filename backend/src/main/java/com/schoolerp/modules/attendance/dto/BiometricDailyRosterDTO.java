package com.schoolerp.modules.attendance.dto;

import com.schoolerp.modules.attendance.entity.BiometricPunchLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricDailyRosterDTO {
    private String tenantId;
    private LocalDate reportDate;
    private int totalPunches;
    private int presentCount;
    private int lateArrivalCount;
    private int earlyDepartureCount;
    private List<BiometricPunchLog> punches;
}

