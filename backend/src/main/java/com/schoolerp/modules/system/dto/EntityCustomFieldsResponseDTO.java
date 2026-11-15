package com.schoolerp.modules.system.dto;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityCustomFieldsResponseDTO {
    private String targetEntityType;
    private UUID targetEntityId;
    private Map<String, String> customFieldValues;
    private Map<String, CustomFieldDefinitionDTO> fieldMetadata;
}

