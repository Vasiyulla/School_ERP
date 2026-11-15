package com.schoolerp.modules.system.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomFieldValueDTO {
    private UUID id;
    private UUID fieldDefinitionId;
    private String fieldCode;
    private String fieldValue;
    private LocalDateTime updatedAt;
}

