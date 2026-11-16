package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.AllergenCheckRequest;
import com.schoolerp.modules.operations.dto.AllergenCheckResultDTO;
import com.schoolerp.modules.operations.service.CafeteriaAllergenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/operations/cafeteria")
@RequiredArgsConstructor
public class CafeteriaAllergenController {

    private final CafeteriaAllergenService cafeteriaAllergenService;

    @PostMapping("/allergen-check")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<AllergenCheckResultDTO>> checkAllergens(
            @RequestBody AllergenCheckRequest request) {
        AllergenCheckResultDTO result = cafeteriaAllergenService.checkAllergens(request);
        return ResponseEntity.ok(ApiResponse.ok("Cafeteria point-of-sale allergen evaluation completed", result));
    }
}
