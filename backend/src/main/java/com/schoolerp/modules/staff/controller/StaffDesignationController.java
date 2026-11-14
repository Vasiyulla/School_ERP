package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.dto.*;
import com.schoolerp.modules.staff.entity.DesignationMaster;
import com.schoolerp.modules.staff.service.DesignationHierarchyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/staff/designations")
@RequiredArgsConstructor
public class StaffDesignationController {

    private final DesignationHierarchyService designationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<DesignationMaster>> createDesignation(
            @RequestBody CreateDesignationRequest request) {
        DesignationMaster created = designationService.createCustomDesignation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Custom staff designation created successfully", created));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'HR_MANAGER', 'ACCOUNTANT', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<DesignationMaster>>> getDesignations(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean activeOnly) {
        List<DesignationMaster> list = designationService.getDesignations(department, activeOnly);
        return ResponseEntity.ok(ApiResponse.ok("Tenant staff designations retrieved successfully", list));
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<DesignationMaster>> toggleStatus(
            @PathVariable UUID id,
            @RequestParam boolean isActive) {
        DesignationMaster updated = designationService.toggleDesignationStatus(id, isActive);
        return ResponseEntity.ok(ApiResponse.ok("Staff designation active status updated successfully", updated));
    }

    @GetMapping("/org-hierarchy")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'HR_MANAGER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<DesignationHierarchyNodeDTO>>> getOrgHierarchy() {
        List<DesignationHierarchyNodeDTO> tree = designationService.getOrganizationalHierarchyTree();
        return ResponseEntity.ok(ApiResponse.ok("Tenant organizational hierarchy tree retrieved successfully", tree));
    }

    @PostMapping("/validate-authority")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'ACCOUNTANT', 'HR_MANAGER')")
    public ResponseEntity<ApiResponse<ApprovalAuthorityResultDTO>> validateAuthority(
            @RequestBody CheckApprovalAuthorityRequest request) {
        ApprovalAuthorityResultDTO result = designationService.validateApprovalAuthority(request);
        return ResponseEntity.ok(ApiResponse.ok("Approval authority threshold evaluated successfully", result));
    }
}

