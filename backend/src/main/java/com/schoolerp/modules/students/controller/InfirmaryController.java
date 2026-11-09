package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.entity.InfirmaryVisit;
import com.schoolerp.modules.students.service.InfirmaryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students/infirmary")
@RequiredArgsConstructor
public class InfirmaryController {

    private final InfirmaryService infirmaryService;

    @Data
    public static class RecordVisitRequest {
        private UUID studentId;
        private String chiefComplaint;
        private Double temperatureFahrenheit;
        private String bloodPressure;
        private Integer pulseBpm;
        private String firstAidGiven;
        private int restDurationMinutes;
        private String disposition;
        private String attendingNurse;
    }

    @PostMapping("/visits")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<InfirmaryVisit>> recordVisit(@RequestBody RecordVisitRequest req) {
        InfirmaryVisit visit = infirmaryService.recordVisit(
                req.getStudentId(),
                req.getChiefComplaint(),
                req.getTemperatureFahrenheit(),
                req.getBloodPressure(),
                req.getPulseBpm(),
                req.getFirstAidGiven(),
                req.getRestDurationMinutes(),
                req.getDisposition(),
                req.getAttendingNurse()
        );
        return ResponseEntity.ok(ApiResponse.ok("Infirmary visit recorded successfully", visit));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'STAFF', 'PARENT')")
    public ResponseEntity<ApiResponse<List<InfirmaryVisit>>> getStudentVisits(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.ok(infirmaryService.getStudentVisits(studentId)));
    }

    @GetMapping("/visits")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<InfirmaryVisit>>> getAllVisits() {
        return ResponseEntity.ok(ApiResponse.ok(infirmaryService.getAllVisits()));
    }
}

