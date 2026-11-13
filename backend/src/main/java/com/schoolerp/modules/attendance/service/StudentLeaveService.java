package com.schoolerp.modules.attendance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.attendance.dto.ApplyStudentLeaveRequest;
import com.schoolerp.modules.attendance.dto.ReviewStudentLeaveRequest;
import com.schoolerp.modules.attendance.entity.AttendanceRecord;
import com.schoolerp.modules.attendance.entity.AttendanceSession;
import com.schoolerp.modules.attendance.entity.StudentLeaveApplication;
import com.schoolerp.modules.attendance.repository.AttendanceRecordRepository;
import com.schoolerp.modules.attendance.repository.AttendanceSessionRepository;
import com.schoolerp.modules.attendance.repository.StudentLeaveApplicationRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentLeaveService {

    private final StudentLeaveApplicationRepository leaveRepository;
    private final StudentRepository studentRepository;
    private final AttendanceSessionRepository sessionRepository;
    private final AttendanceRecordRepository recordRepository;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public StudentLeaveApplication applyLeave(ApplyStudentLeaveRequest request,
                                            UUID parentUserId,
                                            String parentName,
                                            String parentPhone) {
        String tenantId = resolveTenant();

        Student student = studentRepository.findByIdAndTenantId(request.getStudentId(), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + request.getStudentId()));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be prior to start date");
        }

        int totalDays = (int) ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        StudentLeaveApplication app = StudentLeaveApplication.builder()
                .tenantId(tenantId)
                .studentId(student.getId())
                .studentAdmissionNo(student.getAdmissionNumber())
                .studentName(student.getFullName())
                .className(student.getClassName())
                .sectionName(student.getSectionName())
                .parentUserId(parentUserId)
                .parentName(parentName != null ? parentName : student.getGuardianName())
                .parentContactPhone(parentPhone != null ? parentPhone : student.getGuardianPhone())
                .leaveType(request.getLeaveType().toUpperCase())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalDays(totalDays)
                .reason(request.getReason())
                .medicalCertificateUrl(request.getMedicalCertificateUrl())
                .status("PENDING")
                .build();

        return leaveRepository.save(app);
    }

    @Transactional(readOnly = true)
    public List<StudentLeaveApplication> getLeaves(UUID studentId, String status, String className, String sectionName) {
        String tenantId = resolveTenant();

        if (studentId != null) {
            return leaveRepository.findAllByTenantIdAndStudentIdOrderByAppliedAtDesc(tenantId, studentId);
        }
        if (className != null && sectionName != null) {
            return leaveRepository.findAllByTenantIdAndClassNameAndSectionNameOrderByAppliedAtDesc(tenantId, className, sectionName);
        }
        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return leaveRepository.findAllByTenantIdAndStatusOrderByAppliedAtDesc(tenantId, status.toUpperCase());
        }
        return leaveRepository.findAllByTenantIdOrderByAppliedAtDesc(tenantId);
    }

    @Transactional
    public StudentLeaveApplication reviewLeave(UUID applicationId,
                                              ReviewStudentLeaveRequest request,
                                              String reviewerStaffName) {
        String tenantId = resolveTenant();

        StudentLeaveApplication app = leaveRepository.findByIdAndTenantId(applicationId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Student leave application not found with ID: " + applicationId));

        String decision = request.getStatus().toUpperCase();
        app.setStatus(decision);
        app.setReviewRemarks(request.getReviewRemarks());
        app.setReviewedByStaffName(reviewerStaffName);
        app.setReviewedAt(LocalDateTime.now());

        // Attendance Auto-Normalization Engine
        if ("APPROVED".equalsIgnoreCase(decision)) {
            normalizeAttendanceForApprovedLeave(tenantId, app);
        }

        return leaveRepository.save(app);
    }

    private void normalizeAttendanceForApprovedLeave(String tenantId, StudentLeaveApplication app) {
        LocalDate start = app.getStartDate();
        LocalDate end = app.getEndDate();

        List<AttendanceSession> sessions = sessionRepository.findAllByTenantId(tenantId);
        int normalizedCount = 0;

        for (AttendanceSession session : sessions) {
            if (session.getClassName().equalsIgnoreCase(app.getClassName()) &&
                session.getSectionName().equalsIgnoreCase(app.getSectionName()) &&
                !session.getAttendanceDate().isBefore(start) &&
                !session.getAttendanceDate().isAfter(end)) {

                Optional<AttendanceRecord> optRecord = recordRepository.findBySessionIdAndStudentId(session.getId(), app.getStudentId());
                if (optRecord.isPresent()) {
                    AttendanceRecord record = optRecord.get();
                    record.setStatus("EXCUSED");
                    record.setRemarks("Excused Student Leave Approved: " + app.getLeaveType());
                    recordRepository.save(record);
                    normalizedCount++;
                } else {
                    AttendanceRecord record = AttendanceRecord.builder()
                            .tenantId(tenantId)
                            .sessionId(session.getId())
                            .studentId(app.getStudentId())
                            .status("EXCUSED")
                            .remarks("Excused Student Leave Approved: " + app.getLeaveType())
                            .build();
                    recordRepository.save(record);
                    normalizedCount++;
                }
            }
        }

        log.info("Attendance normalized for student {} (Class {}-{}): {} session(s) marked EXCUSED for dates {} to {}",
                app.getStudentName(), app.getClassName(), app.getSectionName(), normalizedCount, start, end);
    }
}

