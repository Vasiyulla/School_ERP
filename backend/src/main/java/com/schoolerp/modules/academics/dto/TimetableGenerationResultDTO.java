package com.schoolerp.modules.academics.dto;

import com.schoolerp.modules.academics.entity.TimetablePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableGenerationResultDTO {
    private String tenantId;
    private String className;
    private String sectionName;
    private int totalPeriodsScheduled;
    private int collisionCount;
    private String status;
    private List<TimetablePeriod> generatedSlots;
    private Map<String, Integer> teacherLoadReport;
}

