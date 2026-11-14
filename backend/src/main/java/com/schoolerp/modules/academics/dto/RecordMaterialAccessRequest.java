package com.schoolerp.modules.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordMaterialAccessRequest {
    private UUID materialId;
    private UUID studentId;
    private String studentName;
    private Integer durationSeconds;
    private Boolean isCompleted;
}

