package com.schoolerp.modules.attendance.service;

import com.schoolerp.modules.attendance.dto.*;
import com.schoolerp.modules.attendance.entity.VirtualAttendanceTelemetry;
import com.schoolerp.modules.attendance.repository.VirtualAttendanceTelemetryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VirtualAttendanceService {

    private final VirtualAttendanceTelemetryRepository telemetryRepository;

    @Transactional
    public VirtualAttendanceTelemetryDTO logPresence(LogVirtualPresenceRequest request) {
        int scheduled = request.getSessionScheduledDurationMinutes() > 0 ? request.getSessionScheduledDurationMinutes() : 45;
        int active = request.getActiveDurationMinutes();

        double attendanceRatio = (double) active / scheduled;
        String status;
        if (attendanceRatio >= 0.70) {
            status = "PRESENT_VIRTUAL";
        } else if (attendanceRatio >= 0.30) {
            status = "PARTIAL_ATTENDANCE_VIRTUAL";
        } else {
            status = "ABSENT_VIRTUAL";
        }

        Optional<VirtualAttendanceTelemetry> existing = telemetryRepository.findBySessionIdAndStudentId(
                request.getSessionId(), request.getStudentId());

        VirtualAttendanceTelemetry record = existing.orElseGet(() -> VirtualAttendanceTelemetry.builder()
                .sessionId(request.getSessionId())
                .studentId(request.getStudentId())
                .studentName(request.getStudentName())
                .className(request.getClassName())
                .joinedAt(LocalDateTime.now().minusMinutes(active))
                .build());

        record.setLeftAt(LocalDateTime.now());
        record.setActiveDurationMinutes(active);
        record.setAttendanceStatus(status);
        record.setConnectionHealthScore(request.getConnectionHealthScore() > 0 ? request.getConnectionHealthScore() : 100);
        record.setLoggedAt(LocalDateTime.now());

        VirtualAttendanceTelemetry saved = telemetryRepository.save(record);
        log.info("Virtual presence logged for {} in session {}: {} (active: {}/{} mins)",
                request.getStudentName(), request.getSessionId(), status, active, scheduled);

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public VirtualSessionAttendanceRosterDTO getSessionAttendanceRoster(UUID sessionId) {
        List<VirtualAttendanceTelemetry> logs = telemetryRepository.findBySessionIdOrderByJoinedAtAsc(sessionId);

        int present = (int) logs.stream().filter(l -> "PRESENT_VIRTUAL".equalsIgnoreCase(l.getAttendanceStatus())).count();
        int partial = (int) logs.stream().filter(l -> "PARTIAL_ATTENDANCE_VIRTUAL".equalsIgnoreCase(l.getAttendanceStatus())).count();
        int absent = (int) logs.stream().filter(l -> "ABSENT_VIRTUAL".equalsIgnoreCase(l.getAttendanceStatus())).count();

        double rate = !logs.isEmpty() ? ((double) present / logs.size()) * 100.0 : 0.0;

        List<VirtualAttendanceTelemetryDTO> dtos = logs.stream().map(this::toDto).collect(Collectors.toList());

        return VirtualSessionAttendanceRosterDTO.builder()
                .sessionId(sessionId)
                .totalAttendees(logs.size())
                .presentVirtualCount(present)
                .partialAttendanceCount(partial)
                .absentCount(absent)
                .classAttendancePercentage(Math.round(rate * 10.0) / 10.0)
                .studentLogs(dtos)
                .build();
    }

    private VirtualAttendanceTelemetryDTO toDto(VirtualAttendanceTelemetry t) {
        return VirtualAttendanceTelemetryDTO.builder()
                .id(t.getId())
                .sessionId(t.getSessionId())
                .studentId(t.getStudentId())
                .studentName(t.getStudentName())
                .className(t.getClassName())
                .joinedAt(t.getJoinedAt())
                .leftAt(t.getLeftAt())
                .activeDurationMinutes(t.getActiveDurationMinutes())
                .attendanceStatus(t.getAttendanceStatus())
                .connectionHealthScore(t.getConnectionHealthScore())
                .loggedAt(t.getLoggedAt())
                .build();
    }
}

