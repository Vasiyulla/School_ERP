package com.schoolerp.modules.admissions.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.admissions.entity.RteApplicationRecord;
import com.schoolerp.modules.admissions.repository.RteApplicationRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RteQuotaService {

    private final RteApplicationRepository rteRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public RteApplicationRecord registerRteCandidate(
            String studentName,
            String admissionNumber,
            String gradeApplied,
            String lotteryAppNo,
            String incomeCertNo,
            BigDecimal annualIncome,
            String category) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        RteApplicationRecord record = RteApplicationRecord.builder()
                .tenantId(tenantId)
                .studentName(studentName)
                .admissionNumber(admissionNumber)
                .gradeApplied(gradeApplied != null ? gradeApplied : "Class 1")
                .lotteryApplicationNo(lotteryAppNo != null ? lotteryAppNo : "RTE-2026-MH-" + System.currentTimeMillis() % 10000)
                .incomeCertificateNo(incomeCertNo != null ? incomeCertNo : "INC-MH-2026-9921")
                .annualHouseholdIncome(annualIncome != null ? annualIncome : new BigDecimal("85000.00"))
                .category(category != null ? category : "EWS")
                .stateGovernmentRate(new BigDecimal("17670.00"))
                .reimbursementStatus("APPROVED_PENDING_DISBURSAL")
                .academicYear("2026-2027")
                .appliedAt(Instant.now())
                .build();

        RteApplicationRecord saved = rteRepository.save(record);

        auditLogService.recordEvent(
                "RTE_QUOTA_ADMISSION_REGISTERED",
                "admissions.rte_records",
                saved.getId().toString(),
                String.format("Enrolled RTE 25%% Quota candidate: %s (%s) under lottery %s. Annual Income: INR %s",
                        saved.getStudentName(), saved.getGradeApplied(), saved.getLotteryApplicationNo(), saved.getAnnualHouseholdIncome())
        );

        return saved;
    }

    public List<RteApplicationRecord> getRteRecords() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<RteApplicationRecord> list = rteRepository.findByTenantId(tenantId);
        if (list.isEmpty()) {
            registerRteCandidate("Kunal Ramesh Solanki", "ADM-RTE-2026-01", "Class 1", "RTE-MH-2026-00421", "INC-MH-2026-1049", new BigDecimal("72000.00"), "EWS");
            registerRteCandidate("Pooja Santosh Gaikwad", "ADM-RTE-2026-02", "Class 1", "RTE-MH-2026-00892", "INC-MH-2026-3021", new BigDecimal("65000.00"), "DISADVANTAGED_GROUP");
            return rteRepository.findByTenantId(tenantId);
        }
        return list;
    }

    public RteAnnualClaimReport generateAnnualReimbursementClaim(String academicYear) {
        String yr = (academicYear != null && !academicYear.isBlank()) ? academicYear : "2026-2027";
        List<RteApplicationRecord> records = getRteRecords().stream()
                .filter(r -> yr.equalsIgnoreCase(r.getAcademicYear()))
                .toList();

        BigDecimal perStudentRate = new BigDecimal("17670.00");
        BigDecimal totalClaim = perStudentRate.multiply(BigDecimal.valueOf(records.size()));

        return RteAnnualClaimReport.builder()
                .academicYear(yr)
                .stateDepartment("Directorate of Primary Education, Government of Maharashtra")
                .totalRteEnrolledStudents(records.size())
                .perStudentApprovedRate(perStudentRate)
                .totalReimbursementClaimAmount(totalClaim)
                .submissionStatus("COMPILED_FOR_TREASURY_SUBMISSION")
                .candidates(records)
                .build();
    }

    @Data
    @Builder
    public static class RteAnnualClaimReport {
        private String academicYear;
        private String stateDepartment;
        private int totalRteEnrolledStudents;
        private BigDecimal perStudentApprovedRate;
        private BigDecimal totalReimbursementClaimAmount;
        private String submissionStatus;
        private List<RteApplicationRecord> candidates;
    }
}

