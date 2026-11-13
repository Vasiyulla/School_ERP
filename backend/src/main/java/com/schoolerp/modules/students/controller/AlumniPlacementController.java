package com.schoolerp.modules.students.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.students.dto.RecordPlacementRequest;
import com.schoolerp.modules.students.entity.AlumniProfile;
import com.schoolerp.modules.students.entity.HigherEdPlacement;
import com.schoolerp.modules.students.service.AlumniPlacementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students/alumni")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Class 12 Alumni & Higher Education Placement Engine", description = "Track graduate alumni, entrance exam ranks (JEE, NEET, CUET, SAT), and university admissions")
public class AlumniPlacementController {

    private final AlumniPlacementService alumniPlacementService;

    @PostMapping("/placements")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Record graduate university placement offer and national entrance test score")
    public ResponseEntity<ApiResponse<HigherEdPlacement>> recordPlacement(
            @Valid @RequestBody RecordPlacementRequest request) {
        HigherEdPlacement placement = alumniPlacementService.recordPlacement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("University placement recorded successfully", placement));
    }

    @GetMapping("/placements")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'STUDENT', 'PARENT')")
    @Operation(summary = "Browse university admissions and entrance examination ranks directory")
    public ResponseEntity<ApiResponse<List<HigherEdPlacement>>> getPlacements(
            @RequestParam(required = false, defaultValue = "ALL") String universityName) {
        List<HigherEdPlacement> list = alumniPlacementService.getPlacements(universityName);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/profiles")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'STUDENT', 'PARENT')")
    @Operation(summary = "List graduate alumni profiles and career status")
    public ResponseEntity<ApiResponse<List<AlumniProfile>>> getProfiles() {
        List<AlumniProfile> profiles = alumniPlacementService.getAlumniProfiles();
        return ResponseEntity.ok(ApiResponse.ok(profiles));
    }
}

