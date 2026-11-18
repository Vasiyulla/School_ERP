package com.schoolerp.modules.admissions.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessSeatSurrenderRequest {

    @NotNull(message = "Application ID is required")
    private UUID applicationId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Grade name is required")
    private String gradeName;

    @NotBlank(message = "Category name is required (GENERAL, RTE, DEFENCE, SIBLING, ALUMNI)")
    private String categoryName;

    @NotBlank(message = "Surrender reason is required")
    private String surrenderReason;

    @NotNull(message = "Surrender date is required")
    private LocalDate surrenderDate;

    @NotNull(message = "Total fees paid is required")
    @DecimalMin(value = "0.00", message = "Total fees paid cannot be negative")
    private BigDecimal totalFeesPaid;

    @NotNull(message = "Deduction percentage is required")
    private BigDecimal deductionPercentage;

    private UUID waitlistStudentPromotedId;

    @NotNull(message = "Authorized by user ID is required")
    private UUID authorizedByUserId;
}
