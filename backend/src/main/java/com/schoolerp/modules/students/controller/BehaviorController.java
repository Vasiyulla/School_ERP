package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.entity.BehaviorIncident;
import com.schoolerp.modules.students.entity.SchoolHouse;
import com.schoolerp.modules.students.service.BehaviorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students/behavior")
@RequiredArgsConstructor
public class BehaviorController {

    private final BehaviorService behaviorService;

    @Data
    public static class RecordIncidentRequest {
        private UUID studentId;
        private String incidentType; // MERIT, DEMERIT
        private String category;
        private int points;
        private String description;
        private String reportedByStaff;
    }

    @PostMapping("/record")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<BehaviorIncident>> recordIncident(@RequestBody RecordIncidentRequest req) {
        BehaviorIncident incident = behaviorService.recordBehaviorIncident(
                req.getStudentId(),
                req.getIncidentType(),
                req.getCategory(),
                req.getPoints(),
                req.getDescription(),
                req.getReportedByStaff()
        );
        return ResponseEntity.ok(ApiResponse.ok("Behavior incident recorded successfully", incident));
    }

    @GetMapping("/houses/leaderboard")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<SchoolHouse>>> getLeaderboard() {
        return ResponseEntity.ok(ApiResponse.ok(behaviorService.getHouseLeaderboard()));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'PARENT', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<BehaviorIncident>>> getStudentIncidents(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.ok(behaviorService.getStudentIncidents(studentId)));
    }
}

