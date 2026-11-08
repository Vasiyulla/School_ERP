package com.schoolerp.modules.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchAttendanceRequest {

    @NotBlank
    private String className;

    @NotBlank
    private String sectionName;

    @Builder.Default
    private LocalDate attendanceDate = LocalDate.now();

    @NotEmpty
    private List<StudentStatusEntry> entries;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentStatusEntry {
        private UUID studentId;
        private String status; // PRESENT, ABSENT, LATE, EXCUSED
        private String remarks;
    }
}

