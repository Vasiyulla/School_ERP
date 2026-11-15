package com.schoolerp.modules.system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldDefinitionDTO {
    private UUID id;
    private String targetEntityType;
    private String fieldCode;
    private String fieldLabel;
    private String fieldType;
    private List<String> options;
    private String defaultValue;
    private String validationRegex;
    private boolean isMandatory;
    private int displayOrder;
    private boolean isActive;
    private LocalDateTime createdAt;
}

