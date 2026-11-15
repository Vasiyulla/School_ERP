package com.schoolerp.modules.system.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomEntityDefinitionRequest {
    private String entityCode; // e.g., "MUN_DELEGATION", "EQUESTRIAN_HORSE"
    private String displayName; // e.g., "Model UN Country Delegation"
    private String pluralName; // e.g., "MUN Delegations"
    private String description;
    private String category; // ACADEMICS, OPERATIONS, ACTIVITIES, SPECIALTY
    private String iconName;
    private List<Map<String, Object>> fieldsSchema; // List of field definitions (name, type, label, required, options)
}

