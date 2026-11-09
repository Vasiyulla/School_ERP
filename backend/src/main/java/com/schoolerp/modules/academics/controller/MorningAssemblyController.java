package com.schoolerp.modules.academics.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.academics.entity.MorningAssemblyRoster;
import com.schoolerp.modules.academics.service.MorningAssemblyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/academics/assemblies")
@RequiredArgsConstructor
public class MorningAssemblyController {

    private final MorningAssemblyService assemblyService;

    @PostMapping("/roster")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("academics.morning_assembly")
    public ResponseEntity<ApiResponse<MorningAssemblyRoster>> scheduleAssembly(@RequestBody ScheduleAssemblyRequest request) {
        MorningAssemblyRoster roster = assemblyService.createRoster(
                request.getWeekStartDate(),
                request.getHouseOnDuty(),
                request.getNewsReaderStudentName(),
                request.getThoughtForTheDay(),
                request.getThoughtPresenterStudentName(),
                request.getPledgeLeaderStudentName(),
                request.getNationalAnthemLeadStudentName(),
                request.getTeacherInChargeName()
        );
        return ResponseEntity.ok(ApiResponse.success(roster, "Morning Assembly quadrangle roster scheduled successfully"));
    }

    @GetMapping("/active-week")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','STUDENT','PARENT')")
    @RequireFeature("academics.morning_assembly")
    public ResponseEntity<ApiResponse<MorningAssemblyRoster>> getActiveWeekRoster() {
        return ResponseEntity.ok(ApiResponse.success(assemblyService.getActiveWeekRoster(), "Active week Morning Assembly roster retrieved"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER')")
    @RequireFeature("academics.morning_assembly")
    public ResponseEntity<ApiResponse<List<MorningAssemblyRoster>>> getAllRosters() {
        return ResponseEntity.ok(ApiResponse.success(assemblyService.getAllRosters(), "Morning Assembly roster history retrieved"));
    }

    @Data
    public static class ScheduleAssemblyRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate weekStartDate;
        private String houseOnDuty;
        private String newsReaderStudentName;
        private String thoughtForTheDay;
        private String thoughtPresenterStudentName;
        private String pledgeLeaderStudentName;
        private String nationalAnthemLeadStudentName;
        private String teacherInChargeName;
    }
}

