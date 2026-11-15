package com.schoolerp.modules.system.dto;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveCustomFieldValueRequest {
    private String targetEntityType; // STUDENT, STAFF, etc.
    private UUID targetEntityId;
    private Map<String, String> fieldValues; // fieldCode -> value
}

