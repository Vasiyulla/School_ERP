package com.schoolerp.modules.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchJobDTO {
    private String jobId;
    private String jobType;
    private String status;
    private int totalItems;
    private int processedItems;
    private int failedItems;
    private double progressPercentage;
    private String errorSummary;
    private String initiatedBy;
    private Instant startedAt;
    private Instant completedAt;
}
