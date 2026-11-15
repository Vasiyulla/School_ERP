package com.schoolerp.modules.admissions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounselorTaskDTO {
    private UUID id;
    private String tenantId;
    private UUID inquiryId;
    private String counselorName;
    private String taskType;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String priority;
    private String status;
    private String outcomeNotes;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}

