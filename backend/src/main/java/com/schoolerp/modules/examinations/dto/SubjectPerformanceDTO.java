package com.schoolerp.modules.examinations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectPerformanceDTO {
    private String subject;
    private int theoryScore;     // Max 80 or normalized 70% component
    private int practicalScore;  // Max 20 or normalized 20% component
    private int internalScore;   // Max 10 or normalized 10% component
    private int totalWeightedScore; // 70/20/10 weighted score out of 100
    private String gradeBand;    // A1, A2, B1, B2, C1, C2, D, E
    private BigDecimal gradePoint; // 10.0, 9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 0.0
    private String remarks;
}
