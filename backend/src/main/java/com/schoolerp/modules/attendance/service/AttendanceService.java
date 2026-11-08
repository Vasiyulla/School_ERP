package com.schoolerp.modules.attendance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.attendance.dto.BatchAttendanceRequest;
import com.schoolerp.modules.attendance.dto.CorrectionRequest;
import com.schoolerp.modules.attendance.entity.AttendanceCorrection;
import com.schoolerp.modules.attendance.entity.AttendanceRecord;
import com.schoolerp.modules.attendance.entity.AttendanceSession;
import com.schoolerp.modules.attendance.repository.AttendanceCorrectionRepository;
import com.schoolerp.modules.attendance.repository.AttendanceRecordRepository;
import com.schoolerp.modules.attendance.repository.AttendanceSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;
    private final AttendanceCorrectionRepository correctionRepository;

    @Transactional
    public AttendanceSession submitBatchAttendance(BatchAttendanceRequest request) {
        String tenantId = TenantContext.getTenantId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String actor = auth != null ? auth.getName() : "teacher";

        LocalDate date = request.getAttendanceDate() != null ? request.getAttendanceDate() : LocalDate.now();

        // 1. Create or retrieve session
        AttendanceSession session = sessionRepository
                .findByTenantIdAndClassNameAndSectionNameAndAttendanceDate(tenantId, request.getClassName(), request.getSectionName(), date)
                .orElseGet(() -> AttendanceSession.builder()
                        .tenantId(tenantId)
                        .className(request.getClassName())
                        .sectionName(request.getSectionName())
                        .attendanceDate(date)
                        .status("LOCKED") // Locked upon submission
                        .submittedBy(actor)
                        .submittedAt(Instant.now())
                        .build());

        AttendanceSession savedSession = sessionRepository.save(session);

        // 2. Batch insert/update records
        for (BatchAttendanceRequest.StudentStatusEntry entry : request.getEntries()) {
            AttendanceRecord record = AttendanceRecord.builder()
                    .tenantId(tenantId)
                    .sessionId(savedSession.getId())
                    .studentId(entry.getStudentId())
                    .status(entry.getStatus().toUpperCase())
                    .remarks(entry.getRemarks())
                    .build();
            recordRepository.save(record);
        }

        return savedSession;
    }

    @Transactional
    public AttendanceCorrection correctAttendance(UUID recordId, CorrectionRequest request) {
        String tenantId = TenantContext.getTenantId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String actor = auth != null ? auth.getName() : "admin";

        AttendanceRecord record = recordRepository.findByIdAndTenantId(recordId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance record not found"));

        String prevStatus = record.getStatus();
        String newStatus = request.getNewStatus().toUpperCase();

        record.setStatus(newStatus);
        recordRepository.save(record);

        // Immutable audit log
        AttendanceCorrection correction = AttendanceCorrection.builder()
                .tenantId(tenantId)
                .attendanceRecordId(recordId)
                .previousStatus(prevStatus)
                .newStatus(newStatus)
                .correctedBy(actor)
                .reason(request.getReason())
                .createdAt(Instant.now())
                .build();

        return correctionRepository.save(correction);
    }

    @Transactional(readOnly = true)
    public List<AttendanceCorrection> getCorrections() {
        return correctionRepository.findAllByTenantIdOrderByCreatedAtDesc(TenantContext.getTenantId());
    }
}

