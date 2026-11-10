package com.schoolerp.modules.system.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.entity.BulkImportJob;
import com.schoolerp.modules.system.repository.BulkImportJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BulkImportService {

    private final StudentRepository studentRepository;
    private final BulkImportJobRepository jobRepository;
    private final AuditLogService auditLogService;

    /**
     * Parses CSV payload with format:
     * admissionNumber,fullName,gender,className,sectionName,guardianName,guardianPhone,guardianEmail
     */
    @Transactional
    public BulkImportJob importStudentsFromCsv(String fileName, String csvContent, String initiatedBy) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        if (csvContent == null || csvContent.isBlank()) {
            throw new IllegalArgumentException("CSV content cannot be empty");
        }

        String[] lines = csvContent.split("\\r?\\n");
        int totalRows = 0;
        int successCount = 0;
        int failedCount = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            // Skip header row if present
            if (i == 0 && line.toLowerCase().startsWith("admissionnumber")) {
                continue;
            }

            totalRows++;
            String[] cols = line.split(",");
            if (cols.length < 5) {
                failedCount++;
                errors.add("Row " + (i + 1) + ": Insufficient columns (expected at least 5)");
                continue;
            }

            String admissionNo = cols[0].trim();
            String fullName = cols[1].trim();
            String gender = cols[2].trim();
            String className = cols[3].trim();
            String sectionName = cols[4].trim();
            String guardianName = cols.length > 5 ? cols[5].trim() : "Parent / Guardian";
            String guardianPhone = cols.length > 6 ? cols[6].trim() : "+91 98200 00000";
            String guardianEmail = cols.length > 7 ? cols[7].trim() : "";

            if (admissionNo.isBlank() || fullName.isBlank()) {
                failedCount++;
                errors.add("Row " + (i + 1) + ": Missing required admissionNumber or fullName");
                continue;
            }

            if (studentRepository.findByAdmissionNumberAndTenantId(admissionNo, tenantId).isPresent()) {
                failedCount++;
                errors.add("Row " + (i + 1) + ": Duplicate admissionNumber '" + admissionNo + "'");
                continue;
            }

            Student student = Student.builder()
                    .tenantId(tenantId)
                    .admissionNumber(admissionNo)
                    .fullName(fullName)
                    .gender(gender.isBlank() ? "Unspecified" : gender)
                    .className(className)
                    .sectionName(sectionName)
                    .rollNumber(totalRows + 100)
                    .status("Active")
                    .guardianName(guardianName)
                    .guardianPhone(guardianPhone)
                    .guardianEmail(guardianEmail)
                    .build();

            studentRepository.save(student);
            successCount++;
        }

        String status = failedCount == 0 ? "COMPLETED" : (successCount > 0 ? "COMPLETED_WITH_ERRORS" : "FAILED");
        String errorSummary = errors.isEmpty() ? "Zero validation errors" : String.join(" | ", errors);
        if (errorSummary.length() > 3900) {
            errorSummary = errorSummary.substring(0, 3900) + "...";
        }

        BulkImportJob job = BulkImportJob.builder()
                .tenantId(tenantId)
                .entityType("STUDENTS")
                .fileName(fileName != null ? fileName : "students_bulk_upload.csv")
                .totalRows(totalRows)
                .successCount(successCount)
                .failedCount(failedCount)
                .status(status)
                .errorDetails(errorSummary)
                .initiatedBy(initiatedBy != null ? initiatedBy : "SUPER_ADMIN")
                .createdAt(Instant.now())
                .build();

        BulkImportJob saved = jobRepository.save(job);

        auditLogService.recordEvent(
                "BULK_STUDENT_CSV_IMPORT",
                "system.bulk_imports",
                saved.getId().toString(),
                String.format("Bulk import '%s': %d succeeded, %d failed out of %d rows",
                        saved.getFileName(), successCount, failedCount, totalRows)
        );

        return saved;
    }

    public BulkImportJob getJob(UUID jobId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return jobRepository.findById(jobId)
                .filter(j -> j.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Import job not found: " + jobId));
    }

    public List<BulkImportJob> getJobs() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return jobRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
    }
}

