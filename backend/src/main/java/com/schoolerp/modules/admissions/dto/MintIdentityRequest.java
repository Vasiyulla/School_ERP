package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MintIdentityRequest {

    @NotNull(message = "Student ID is required")
    private UUID studentId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Grade name is required")
    private String gradeName;

    @NotBlank(message = "Section name is required")
    private String sectionName;

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    @NotBlank(message = "House name is required")
    private String houseName;

    private String smartCardRfidUid;

    private BigDecimal canteenInitialBalance;

    @NotNull(message = "Minted by user ID is required")
    private UUID mintedByUserId;
}
