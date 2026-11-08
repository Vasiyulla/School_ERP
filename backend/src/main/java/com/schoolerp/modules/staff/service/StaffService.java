package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.StaffLeaveRequest;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.staff.repository.StaffLeaveRequestRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffService {

    private final EmployeeRepository employeeRepository;
    private final StaffLeaveRequestRepository leaveRequestRepository;
    private final AuditLogService auditLogService;

    public List<Employee> getEmployees() {
        return employeeRepository.findByTenantId(TenantContext.getTenantId());
    }

    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .filter(e -> e.getTenantId().equals(TenantContext.getTenantId()))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + id));
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        String tenantId = TenantContext.getTenantId();
        employee.setTenantId(tenantId);
        Employee saved = employeeRepository.save(employee);

        auditLogService.recordEvent(
                "CREATE",
                "staff.employees",
                saved.getEmployeeCode(),
                "New employee profile created: " + saved.getFullName() + " (" + saved.getDesignation() + ")"
        );
        return saved;
    }

    public List<StaffLeaveRequest> getLeaveRequests() {
        return leaveRequestRepository.findByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public StaffLeaveRequest applyLeave(StaffLeaveRequest request) {
        String tenantId = TenantContext.getTenantId();
        request.setTenantId(tenantId);
        request.setStatus("PENDING");
        StaffLeaveRequest saved = leaveRequestRepository.save(request);

        auditLogService.recordEvent(
                "APPLY_LEAVE",
                "staff.leave_requests",
                saved.getId().toString(),
                "Leave request submitted by " + saved.getEmployeeName() + " for " + saved.getLeaveType()
        );
        return saved;
    }

    @Transactional
    public StaffLeaveRequest reviewLeave(UUID leaveId, String status, String remarks) {
        String tenantId = TenantContext.getTenantId();
        StaffLeaveRequest leave = leaveRequestRepository.findByIdAndTenantId(leaveId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found: " + leaveId));

        String reviewer = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "Principal Office";

        leave.setStatus(status);
        leave.setReviewerRemarks(remarks);
        leave.setReviewedBy(reviewer);
        leave.setReviewedAt(Instant.now());

        StaffLeaveRequest updated = leaveRequestRepository.save(leave);

        auditLogService.recordEvent(
                "REVIEW_LEAVE",
                "staff.leave_requests",
                updated.getId().toString(),
                "Leave request for " + updated.getEmployeeName() + " marked as " + status + " with remarks: " + remarks
        );
        return updated;
    }
}

