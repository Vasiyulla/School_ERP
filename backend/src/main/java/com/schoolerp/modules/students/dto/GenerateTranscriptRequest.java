package com.schoolerp.modules.students.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTranscriptRequest {
    private String academicStream; // e.g. "Science (Physics, Chemistry, Math, Biology)"
    private BigDecimal cumulativeGpa; // e.g. 9.40
    private BigDecimal cumulativePercentage; // e.g. 94.20%
    private int totalCredits; // e.g. 40
    private String mediumOfInstruction; // "English"
    private String issuedByPrincipal;
}

