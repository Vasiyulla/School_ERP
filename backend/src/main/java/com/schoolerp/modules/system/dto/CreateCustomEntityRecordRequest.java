package com.schoolerp.modules.system.dto;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomEntityRecordRequest {
    private String recordTitle; // Primary identifier e.g. "USA - UN Security Council" or "Thunder (Stallion)"
    private Map<String, Object> data; // Dynamic key-values matching fieldsSchema
}

