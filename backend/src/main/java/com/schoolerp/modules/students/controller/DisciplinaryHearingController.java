package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.dto.DisciplinaryDTOs;
import com.schoolerp.modules.students.entity.DisciplinaryHearing;
import com.schoolerp.modules.students.entity.DisciplinarySanction;
import com.schoolerp.modules.students.service.DisciplinaryHearingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students/discipline")
@RequiredArgsConstructor
public class DisciplinaryHearingController {

    private final DisciplinaryHearingService hearingService;

    @PostMapping("/hearings")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<DisciplinaryHearing>> conveneHearing(
            @RequestBody DisciplinaryDTOs.ConveneHearingRequest request) {
        DisciplinaryHearing hearing = hearingService.conveneHearing(request);
        return ResponseEntity.ok(ApiResponse.ok("Formal disciplinary hearing docket convened", hearing));
    }

    @PutMapping("/hearings/{id}/sanction")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<DisciplinarySanction>> recordSanction(
            @PathVariable UUID id,
            @RequestBody DisciplinaryDTOs.RecordSanctionRequest request,
            Principal principal) {
        String authorizedBy = principal != null ? principal.getName() : "Principal";
        DisciplinarySanction sanction = hearingService.recordSanction(id, request, authorizedBy);
        return ResponseEntity.ok(ApiResponse.ok("Disciplinary sanction recorded and enforced", sanction));
    }

    @GetMapping("/hearings/student/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DisciplinaryDTOs.StudentDisciplinaryHistoryDTO>> getStudentDisciplinaryHistory(
            @PathVariable UUID id) {
        DisciplinaryDTOs.StudentDisciplinaryHistoryDTO history = hearingService.getStudentDisciplinaryHistory(id);
        return ResponseEntity.ok(ApiResponse.ok("Student disciplinary and counseling history retrieved", history));
    }
}
