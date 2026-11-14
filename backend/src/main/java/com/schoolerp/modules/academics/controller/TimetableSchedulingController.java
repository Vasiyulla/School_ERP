package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.dto.AutoGenerateTimetableRequest;
import com.schoolerp.modules.academics.dto.TimetableGenerationResultDTO;
import com.schoolerp.modules.academics.entity.TimetablePeriod;
import com.schoolerp.modules.academics.service.TimetableSchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academics/timetable")
@RequiredArgsConstructor
public class TimetableSchedulingController {

    private final TimetableSchedulingService timetableService;

    @PostMapping("/auto-generate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'ACADEMIC_COORDINATOR')")
    public ResponseEntity<ApiResponse<TimetableGenerationResultDTO>> autoGenerateTimetable(
            @RequestBody AutoGenerateTimetableRequest request) {
        TimetableGenerationResultDTO result = timetableService.autoGenerateClassTimetable(request);
        return ResponseEntity.ok(ApiResponse.ok("Master collision-free timetable generated successfully", result));
    }

    @GetMapping("/teacher-schedule/{teacherName}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'ACADEMIC_COORDINATOR')")
    public ResponseEntity<ApiResponse<List<TimetablePeriod>>> getTeacherSchedule(
            @PathVariable String teacherName) {
        List<TimetablePeriod> schedule = timetableService.getTeacherWeeklySchedule(teacherName);
        return ResponseEntity.ok(ApiResponse.ok("Teacher weekly timetable retrieved successfully", schedule));
    }
}
