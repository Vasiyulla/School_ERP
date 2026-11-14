package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.staff.dto.CpdComplianceSummaryDTO;
import com.schoolerp.modules.staff.dto.CpdSchoolAuditReportDTO;
import com.schoolerp.modules.staff.dto.LogCpdTrainingRequest;
import com.schoolerp.modules.staff.entity.TeacherCpdRecord;
import com.schoolerp.modules.staff.repository.TeacherCpdRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherCpdService {

    private final TeacherCpdRecordRepository cpdRepository;

    private static final int MANDATORY_ANNUAL_CPD_HOURS = 50;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public TeacherCpdRecord logTraining(LogCpdTrainingRequest request, String approver) {
        String tenantId = resolveTenant();

        TeacherCpdRecord record = TeacherCpdRecord.builder()
                .tenantId(tenantId)
                .staffId(request.getStaffId() != null ? request.getStaffId() : UUID.randomUUID())
                .teacherName(request.getTeacherName() != null ? request.getTeacherName() : "Faculty Member")
                .trainingYear(request.getTrainingYear() != null ? request.getTrainingYear() : "2026-2027")
                .agency(request.getAgency() != null ? request.getAgency().toUpperCase() : "CBSE_COE")
                .trainingTopic(request.getTrainingTopic() != null ? request.getTrainingTopic() : "Competency Based Pedagogy")
                .domain(request.getDomain() != null ? request.getDomain().toUpperCase() : "PEDAGOGY")
                .durationHours(request.getDurationHours() != null ? request.getDurationHours() : 5)
                .completionDate(request.getCompletionDate() != null ? request.getCompletionDate() : java.time.LocalDate.now())
                .certificateRef(request.getCertificateRef() != null ? request.getCertificateRef() : "CERT-CBSE-" + System.currentTimeMillis())
                .evaluationScore(request.getEvaluationScore() != null ? request.getEvaluationScore() : 88.5)
                .verifiedBy(approver)
                .build();

        TeacherCpdRecord saved = cpdRepository.save(record);
        log.info("Logged {} hours of CPD training ({}) for teacher {}",
                saved.getDurationHours(), saved.getTrainingTopic(), saved.getTeacherName());
        return saved;
    }

    @Transactional(readOnly = true)
    public CpdSchoolAuditReportDTO getSchoolCpdComplianceSummary(String trainingYear) {
        String tenantId = resolveTenant();
        String year = (trainingYear != null && !trainingYear.isBlank()) ? trainingYear : "2026-2027";

        List<TeacherCpdRecord> records = cpdRepository.findAllByTenantIdAndTrainingYear(tenantId, year);

        Map<UUID, List<TeacherCpdRecord>> recordsByStaff = records.stream()
                .collect(Collectors.groupingBy(TeacherCpdRecord::getStaffId));

        List<CpdComplianceSummaryDTO> summaries = new ArrayList<>();
        int compliant = 0;
        int inProgress = 0;
        int deficit = 0;

        for (Map.Entry<UUID, List<TeacherCpdRecord>> entry : recordsByStaff.entrySet()) {
            UUID staffId = entry.getKey();
            List<TeacherCpdRecord> staffRecords = entry.getValue();
            String name = staffRecords.isEmpty() ? "Teacher" : staffRecords.get(0).getTeacherName();
            int totalHours = staffRecords.stream().mapToInt(TeacherCpdRecord::getDurationHours).sum();

            String status;
            if (totalHours >= MANDATORY_ANNUAL_CPD_HOURS) {
                status = "COMPLIANT";
                compliant++;
            } else if (totalHours >= 20) {
                status = "IN_PROGRESS";
                inProgress++;
            } else {
                status = "DEFICIT";
                deficit++;
            }

            int remaining = Math.max(0, MANDATORY_ANNUAL_CPD_HOURS - totalHours);
            double pct = Math.min(100.0, ((double) totalHours / MANDATORY_ANNUAL_CPD_HOURS) * 100.0);

            summaries.add(CpdComplianceSummaryDTO.builder()
                    .staffId(staffId)
                    .teacherName(name)
                    .totalCompletedHours(totalHours)
                    .mandatoryTargetHours(MANDATORY_ANNUAL_CPD_HOURS)
                    .remainingHours(remaining)
                    .compliancePercentage(Math.round(pct * 10.0) / 10.0)
                    .status(status)
                    .build());
        }

        int totalTeachers = recordsByStaff.size();
        double complianceRate = totalTeachers > 0 ? ((double) compliant / totalTeachers) * 100.0 : 0.0;

        return CpdSchoolAuditReportDTO.builder()
                .tenantId(tenantId)
                .trainingYear(year)
                .totalTeachersAudited(totalTeachers)
                .compliantCount(compliant)
                .inProgressCount(inProgress)
                .deficitCount(deficit)
                .schoolComplianceRate(Math.round(complianceRate * 10.0) / 10.0)
                .teacherSummaries(summaries)
                .build();
    }
}

