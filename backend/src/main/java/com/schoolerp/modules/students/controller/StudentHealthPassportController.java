package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.dto.SaveHealthPassportRequest;
import com.schoolerp.modules.students.dto.StudentHealthPassportDTO;
import com.schoolerp.modules.students.service.HealthPassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentHealthPassportController {

    private final HealthPassportService healthPassportService;

    @PostMapping("/{id}/health-passport")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('PARENT') or hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<StudentHealthPassportDTO>> saveHealthPassport(
            @PathVariable UUID id,
            @RequestBody SaveHealthPassportRequest request) {
        StudentHealthPassportDTO passport = healthPassportService.saveHealthPassport(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Critical Health Passport updated and medical verification recorded", passport));
    }

    @GetMapping("/{id}/health-passport")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('PARENT') or hasRole('TEACHER') or hasRole('DOCTOR')")
    public ResponseEntity<ApiResponse<StudentHealthPassportDTO>> getHealthPassport(@PathVariable UUID id) {
        StudentHealthPassportDTO passport = healthPassportService.getHealthPassport(id);
        return ResponseEntity.ok(ApiResponse.ok("Student Critical Health Passport retrieved", passport));
    }
}
