package com.schoolerp.modules.attendance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.attendance.dto.BiometricDailyRosterDTO;
import com.schoolerp.modules.attendance.dto.BiometricPunchPayloadItemDTO;
import com.schoolerp.modules.attendance.dto.BiometricPunchWebhookRequest;
import com.schoolerp.modules.attendance.dto.BiometricSyncResultDTO;
import com.schoolerp.modules.attendance.entity.BiometricPunchLog;
import com.schoolerp.modules.attendance.repository.BiometricPunchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiometricSyncService {

    private final BiometricPunchLogRepository punchLogRepository;

    private static final LocalTime MORNING_ON_TIME_CUTOFF = LocalTime.of(8, 15);
    private static final LocalTime EVENING_DEPARTURE_CUTOFF = LocalTime.of(15, 30);

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public BiometricSyncResultDTO ingestBiometricPunches(BiometricPunchWebhookRequest request) {
        String tenantId = resolveTenant();
        String deviceSerial = (request.getDeviceSerialNumber() != null) ? request.getDeviceSerialNumber() : "eSSL-DEVICE-01";
        String ip = (request.getRawIpAddress() != null) ? request.getRawIpAddress() : "192.168.1.120";

        List<BiometricPunchLog> logsToSave = new ArrayList<>();
        int present = 0;
        int lateArrival = 0;
        int earlyDeparture = 0;

        if (request.getPunches() != null) {
            for (BiometricPunchPayloadItemDTO punch : request.getPunches()) {
                LocalDateTime punchTime = (punch.getPunchTime() != null) ? punch.getPunchTime() : LocalDateTime.now();
                String punchType = (punch.getPunchType() != null) ? punch.getPunchType().toUpperCase() : "IN";
                LocalTime timeOfDay = punchTime.toLocalTime();

                String status;
                if ("IN".equals(punchType)) {
                    if (timeOfDay.isBefore(MORNING_ON_TIME_CUTOFF) || timeOfDay.equals(MORNING_ON_TIME_CUTOFF)) {
                        status = "PRESENT";
                        present++;
                    } else {
                        status = "LATE_ARRIVAL";
                        lateArrival++;
                    }
                } else {
                    if (timeOfDay.isBefore(EVENING_DEPARTURE_CUTOFF)) {
                        status = "EARLY_DEPARTURE";
                        earlyDeparture++;
                    } else {
                        status = "PRESENT";
                        present++;
                    }
                }

                BiometricPunchLog logEntry = BiometricPunchLog.builder()
                        .tenantId(tenantId)
                        .deviceSerialNumber(deviceSerial)
                        .staffId(UUID.randomUUID())
                        .staffCode(punch.getStaffCode() != null ? punch.getStaffCode() : "STAFF-EMP-" + System.currentTimeMillis() % 1000)
                        .staffName(punch.getStaffName() != null ? punch.getStaffName() : "Faculty Member")
                        .punchTimestamp(punchTime)
                        .punchType(punchType)
                        .attendanceStatus(status)
                        .ipAddress(ip)
                        .processed(true)
                        .build();

                logsToSave.add(logEntry);
            }
        }

        List<BiometricPunchLog> saved = punchLogRepository.saveAll(logsToSave);
        log.info("Ingested {} biometric punches from device {}. Present: {}, Late: {}, Early: {}",
                saved.size(), deviceSerial, present, lateArrival, earlyDeparture);

        return BiometricSyncResultDTO.builder()
                .tenantId(tenantId)
                .deviceSerialNumber(deviceSerial)
                .totalPunchesReceived(saved.size())
                .presentCount(present)
                .lateArrivalCount(lateArrival)
                .earlyDepartureCount(earlyDeparture)
                .processedLogs(saved)
                .build();
    }

    @Transactional(readOnly = true)
    public BiometricDailyRosterDTO getDailyBiometricSummary(LocalDate date) {
        String tenantId = resolveTenant();
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.atTime(LocalTime.MAX);

        List<BiometricPunchLog> logs = punchLogRepository.findAllByTenantIdAndPunchTimestampBetween(tenantId, start, end);

        int present = 0;
        int lateArrival = 0;
        int earlyDeparture = 0;

        for (BiometricPunchLog logEntry : logs) {
            switch (logEntry.getAttendanceStatus()) {
                case "PRESENT" -> present++;
                case "LATE_ARRIVAL" -> lateArrival++;
                case "EARLY_DEPARTURE" -> earlyDeparture++;
                default -> {}
            }
        }

        return BiometricDailyRosterDTO.builder()
                .tenantId(tenantId)
                .reportDate(targetDate)
                .totalPunches(logs.size())
                .presentCount(present)
                .lateArrivalCount(lateArrival)
                .earlyDepartureCount(earlyDeparture)
                .punches(logs)
                .build();
    }
}

