package com.schoolerp.modules.system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEntityDefinitionDTO {
    private UUID id;
    private String entityCode;
    private String displayName;
    private String pluralName;
    private String description;
    private String category;
    private String iconName;
    private List<Map<String, Object>> fieldsSchema;
    private boolean isPublished;
    private int recordCount;
    private LocalDateTime createdAt;
}

