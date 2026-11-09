package com.schoolerp.modules.staff.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.StaffLeaveRequest;
import com.schoolerp.modules.staff.service.StaffLeaveService;
import com.schoolerp.modules.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;
    private final StaffLeaveService staffLeaveService;

    @GetMapping("/employees")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<Employee>>> getEmployees() {
        return ResponseEntity.ok(ApiResponse.ok(staffService.getEmployees()));
    }

    @GetMapping("/employees/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<Employee>> getEmployee(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(staffService.getEmployeeById(id)));
    }

    @PostMapping("/employees")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<Employee>> createEmployee(@RequestBody Employee employee) {
        Employee created = staffService.createEmployee(employee);
        return ResponseEntity.ok(ApiResponse.ok("Employee registered successfully", created));
    }

    @GetMapping("/leaves")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<StaffLeaveRequest>>> getLeaveRequests() {
        return ResponseEntity.ok(ApiResponse.ok(staffService.getLeaveRequests()));
    }

    @GetMapping("/leaves/pending")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<List<StaffLeaveRequest>>> getPendingLeaves() {
        return ResponseEntity.ok(ApiResponse.ok(staffLeaveService.getPendingLeaves()));
    }

    @GetMapping("/leaves/employee/{employeeId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<StaffLeaveRequest>>> getEmployeeLeaves(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(ApiResponse.ok(staffLeaveService.getEmployeeLeaves(employeeId)));
    }

    @PostMapping("/leaves")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<StaffLeaveRequest>> applyLeave(@RequestBody StaffLeaveRequest request) {
        StaffLeaveRequest applied = staffService.applyLeave(request);
        return ResponseEntity.ok(ApiResponse.ok("Leave application submitted", applied));
    }

    @PostMapping("/leaves/{id}/review")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<StaffLeaveRequest>> reviewLeave(
            @PathVariable UUID id,
            @RequestBody ReviewLeaveDto dto) {
        StaffLeaveRequest reviewed = staffService.reviewLeave(id, dto.getStatus(), dto.getRemarks());
        return ResponseEntity.ok(ApiResponse.ok("Leave request processed", reviewed));
    }

    @Data
    public static class ReviewLeaveDto {
        private String status; // APPROVED, REJECTED
        private String remarks;
    }
}

