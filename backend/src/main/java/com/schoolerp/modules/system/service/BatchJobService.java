package com.schoolerp.modules.system.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.system.dto.BatchJobDTO;
import com.schoolerp.modules.system.entity.BatchJobExecution;
import com.schoolerp.modules.system.repository.BatchJobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchJobService {

    private final BatchJobExecutionRepository batchJobExecutionRepository;

    @Transactional
    public BatchJobDTO submitJob(String jobType, int totalItems, String initiatedBy) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "apex";
        }

        long count = batchJobExecutionRepository.countByTenantId(tenantId) + 1;
        String jobId = String.format("JOB-2026-%04d", count);

        BatchJobExecution execution = BatchJobExecution.builder()
                .jobId(jobId)
                .tenantId(tenantId)
                .jobType(jobType != null ? jobType : "GENERIC_ASYNC_TASK")
                .status("SUBMITTED")
                .totalItems(totalItems > 0 ? totalItems : 100)
                .processedItems(0)
                .failedItems(0)
                .progressPercentage(0.0)
                .initiatedBy(initiatedBy != null ? initiatedBy : "admin")
                .startedAt(Instant.now())
                .build();

        BatchJobExecution saved = batchJobExecutionRepository.save(execution);
        log.info("Batch job [{}] submitted for tenant [{}] with [{}] total items", jobId, tenantId, totalItems);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public BatchJobDTO getJobStatus(String jobId) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = "apex";
        }

        BatchJobExecution execution = batchJobExecutionRepository.findByJobIdAndTenantId(jobId, tenantId)
                .or(() -> batchJobExecutionRepository.findByJobId(jobId))
                .orElseThrow(() -> new NoSuchElementException("Batch job not found with ID: " + jobId));

        return toDTO(execution);
    }

    @Transactional
    public BatchJobDTO runSimulatedBulkJob(String jobType, int totalItems, String initiatedBy) {
        BatchJobDTO dto = submitJob(jobType, totalItems, initiatedBy);
        String jobId = dto.getJobId();

        // Process synchronously up to completion for seamless testing and demonstration
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        BatchJobExecution execution = batchJobExecutionRepository.findByJobId(jobId)
                .orElseThrow();

        execution.setStatus("COMPLETED");
        execution.setProcessedItems(execution.getTotalItems());
        execution.setProgressPercentage(100.0);
        execution.setCompletedAt(Instant.now());
        batchJobExecutionRepository.save(execution);

        return toDTO(execution);
    }

    private BatchJobDTO toDTO(BatchJobExecution e) {
        return BatchJobDTO.builder()
                .jobId(e.getJobId())
                .jobType(e.getJobType())
                .status(e.getStatus())
                .totalItems(e.getTotalItems())
                .processedItems(e.getProcessedItems())
                .failedItems(e.getFailedItems())
                .progressPercentage(e.getProgressPercentage())
                .errorSummary(e.getErrorSummary())
                .initiatedBy(e.getInitiatedBy())
                .startedAt(e.getStartedAt())
                .completedAt(e.getCompletedAt())
                .build();
    }
}
