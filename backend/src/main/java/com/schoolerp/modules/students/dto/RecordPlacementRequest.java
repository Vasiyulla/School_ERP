package com.schoolerp.modules.students.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordPlacementRequest {

    @NotNull(message = "Student ID is mandatory")
    private UUID studentId;

    @NotBlank(message = "University name is mandatory")
    private String universityName; // e.g. "IIT Bombay", "BITS Pilani", "Stanford"

    @Builder.Default
    private String country = "India";

    @NotBlank(message = "Degree program is mandatory")
    private String degreeProgram; // e.g. "B.Tech Computer Science"

    @Builder.Default
    private String admissionType = "ENTRANCE_RANK"; // DIRECT_MERIT, ENTRANCE_RANK, SCHOLARSHIP

    private String entranceExamName; // e.g. "JEE Advanced", "NEET-UG", "SAT"

    private String entranceScoreOrRank; // e.g. "AIR 142"

    @Builder.Default
    private BigDecimal scholarshipAmountInr = BigDecimal.ZERO;
}

