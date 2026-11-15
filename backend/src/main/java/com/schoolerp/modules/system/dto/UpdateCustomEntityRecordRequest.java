package com.schoolerp.modules.system.dto;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomEntityRecordRequest {
    private String recordTitle;
    private String status; // ACTIVE, ARCHIVED
    private Map<String, Object> data;
}

