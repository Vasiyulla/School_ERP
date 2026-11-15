package com.schoolerp.modules.students.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficialTranscriptDTO {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String transcriptNumber;
    private String academicStream;
    private BigDecimal cumulativeGpa;
    private BigDecimal cumulativePercentage;
    private int totalCredits;
    private String mediumOfInstruction;
    private String verificationHash;
    private LocalDate issuedDate;
    private String issuedByPrincipal;
}

