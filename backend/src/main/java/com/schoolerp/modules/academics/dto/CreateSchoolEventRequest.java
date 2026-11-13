package com.schoolerp.modules.academics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSchoolEventRequest {

    @NotBlank(message = "Event title is mandatory")
    private String title;

    private String description;

    @NotBlank(message = "Category is mandatory (e.g. SPORTS, CULTURAL, ACADEMIC_OLYMPIAD, SCIENCE_EXHIBITION)")
    private String category;

    @NotNull(message = "Event date is mandatory")
    private LocalDate eventDate;

    private LocalDate endDate;

    @NotBlank(message = "Venue is mandatory")
    private String venue;

    private String coordinatorTeacherName;

    @Builder.Default
    private String targetClasses = "ALL";

    @Builder.Default
    private Integer maxParticipants = 100;

    @Builder.Default
    private BigDecimal budgetAllocated = BigDecimal.ZERO;
}

