package com.schoolerp.modules.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialEngagementReportDTO {
    private UUID materialId;
    private String title;
    private String subjectName;
    private String className;
    private int totalViews;
    private int uniqueStudentsViewed;
    private double averageDurationSeconds;
    private List<StudentAccessDetailDTO> studentAccessList;
}

