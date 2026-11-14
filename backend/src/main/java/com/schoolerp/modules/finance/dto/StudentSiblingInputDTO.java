package com.schoolerp.modules.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSiblingInputDTO {
    private UUID studentId;
    private String admissionNumber;
    private String studentName;
    private LocalDate dateOfBirth;
    private BigDecimal standardTuitionFee;
}

