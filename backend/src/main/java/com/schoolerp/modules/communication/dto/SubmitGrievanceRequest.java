package com.schoolerp.modules.communication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitGrievanceRequest {

    private String parentName;

    private String parentPhone;

    private String studentName;

    private String className;

    @NotBlank(message = "Category is mandatory (ACADEMICS_GRADING, TRANSPORT_BUS, CANTEEN_FOOD, FACULTY_BEHAVIOR, FEE_ACCOUNTS, INFRASTRUCTURE)")
    private String category;

    @NotBlank(message = "Subject is mandatory")
    private String subject;

    @NotBlank(message = "Description is mandatory")
    private String description;
}

