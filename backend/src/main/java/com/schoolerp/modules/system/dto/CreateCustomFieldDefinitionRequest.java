package com.schoolerp.modules.system.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomFieldDefinitionRequest {
    private String targetEntityType; // STUDENT, STAFF, ADMISSION, INVOICE, BOOK, VEHICLE
    private String fieldCode;
    private String fieldLabel;
    private String fieldType; // TEXT, NUMBER, DROPDOWN, BOOLEAN, DATE, FILE_URL
    private List<String> options;
    private String defaultValue;
    private String validationRegex;
    private Boolean isMandatory;
    private Integer displayOrder;
}

