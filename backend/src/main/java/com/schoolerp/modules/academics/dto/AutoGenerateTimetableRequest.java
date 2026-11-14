package com.schoolerp.modules.academics.dto;

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
public class AutoGenerateTimetableRequest {
    private String academicYear;
    private String className;
    private String sectionName;
    private List<String> subjects;
    private Map<String, String> teacherAssignments; // subject -> teacherName
    private Map<String, String> roomAssignments;    // subject -> roomNumber
    private Integer periodsPerDay;                 // Default 7
    private List<String> daysOfWeek;               // Monday to Friday
}

