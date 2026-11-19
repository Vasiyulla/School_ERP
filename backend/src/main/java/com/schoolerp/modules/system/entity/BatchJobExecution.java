package com.schoolerp.modules.system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "batch_job_executions", schema = "system", indexes = {
        @Index(name = "idx_batch_job_id", columnList = "job_id, tenant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchJobExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "job_id", nullable = false, unique = true)
    private String jobId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "job_type", nullable = false)
    private String jobType;

    @Column(name = "status", nullable = false)
    private String status; // SUBMITTED, PROCESSING, COMPLETED, FAILED, CANCELLED

    @Column(name = "total_items", nullable = false)
    private int totalItems;

    @Column(name = "processed_items", nullable = false)
    private int processedItems;

    @Column(name = "failed_items", nullable = false)
    private int failedItems;

    @Column(name = "progress_percentage", nullable = false)
    private double progressPercentage;

    @Column(name = "error_summary", length = 1000)
    private String errorSummary;

    @Column(name = "initiated_by")
    private String initiatedBy;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
