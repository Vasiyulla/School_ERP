package com.schoolerp.modules.system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEntityRecordDTO {
    private UUID id;
    private UUID customEntityDefinitionId;
    private String entityCode;
    private String recordTitle;
    private String status;
    private Map<String, Object> data;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

