package com.schoolerp.modules.students.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollStudentRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    @NotBlank(message = "Class name is required")
    private String className;
    @NotBlank(message = "Section name is required")
    private String sectionName;
    private int rollNumber;
    private String guardianName;
    private String guardianPhone;
    private String guardianEmail;
    private String lockerIndex;
}

