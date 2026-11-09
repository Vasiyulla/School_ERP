package com.schoolerp.modules.staff.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.SubstitutionRecommendationDTO;
import com.schoolerp.modules.academics.entity.TeacherSubstitution;
import com.schoolerp.modules.academics.service.SubstitutionService;
import com.schoolerp.modules.communication.service.CommunicationService;
import com.schoolerp.modules.staff.entity.Employee;
import com.schoolerp.modules.staff.entity.StaffLeaveRequest;
import com.schoolerp.modules.staff.repository.EmployeeRepository;
import com.schoolerp.modules.staff.repository.StaffLeaveRequestRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffLeaveService {

    private final StaffLeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final SubstitutionService substitutionService;
    private final CommunicationService communicationService;
    private final AuditLogService auditLogService;

    @Transactional
    public StaffLeaveRequest applyLeave(UUID employeeId, String leaveType, LocalDate startDate, LocalDate endDate, String reason) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        Employee employee = employeeRepository.findById(employeeId)
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        StaffLeaveRequest req = StaffLeaveRequest.builder()
                .tenantId(tenantId)
                .employeeId(employee.getId())
                .employeeName(employee.getFullName())
                .leaveType(leaveType != null ? leaveType.toUpperCase() : "CASUAL")
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .status("PENDING")
                .createdAt(Instant.now())
                .build();

        StaffLeaveRequest saved = leaveRequestRepository.save(req);
        auditLogService.logAction(employee.getEmail(), "APPLY_STAFF_LEAVE", "staff.staff_leave_requests", saved.getId().toString(),
                "Applied for " + saved.getLeaveType() + " leave from " + startDate + " to " + endDate);

        return saved;
    }

    public List<StaffLeaveRequest> getPendingLeaves() {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return leaveRequestRepository.findByTenantIdAndStatus(tenantId, "PENDING");
    }

    public List<StaffLeaveRequest> getEmployeeLeaves(UUID employeeId) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return leaveRequestRepository.findByTenantIdAndEmployeeId(tenantId, employeeId);
    }

    @Transactional
    public StaffLeaveReviewResult reviewLeave(UUID leaveRequestId, boolean approve, String remarks, String reviewerEmail) {
        final String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        StaffLeaveRequest req = leaveRequestRepository.findByIdAndTenantId(leaveRequestId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found: " + leaveRequestId));

        req.setStatus(approve ? "APPROVED" : "REJECTED");
        req.setReviewerRemarks(remarks != null ? remarks : (approve ? "Approved by Principal" : "Rejected"));
        req.setReviewedBy(reviewerEmail != null ? reviewerEmail : "principal@apexacademy.edu.in");
        req.setReviewedAt(Instant.now());

        StaffLeaveRequest saved = leaveRequestRepository.save(req);

        List<TeacherSubstitution> triggeredSubstitutions = new ArrayList<>();

        // If Approved and the staff member is a teacher, auto-trigger the substitution board
        if (approve) {
            LocalDate current = req.getStartDate();
            while (!current.isAfter(req.getEndDate())) {
                try {
                    List<SubstitutionRecommendationDTO> recs = substitutionService.getRecommendations(current, req.getEmployeeId());
                    for (SubstitutionRecommendationDTO rec : recs) {
                        if (rec.getCandidateSubstitutes() != null && !rec.getCandidateSubstitutes().isEmpty()) {
                            // Pick top candidate
                            var topSub = rec.getCandidateSubstitutes().get(0);
                            TeacherSubstitution sub = substitutionService.assignSubstitution(
                                    current,
                                    rec.getPeriodNumber(),
                                    rec.getTimeSlot(),
                                    rec.getClassName(),
                                    rec.getSectionName(),
                                    rec.getSubject(),
                                    rec.getRoomNumber(),
                                    req.getEmployeeId(),
                                    req.getEmployeeName(),
                                    topSub.getTeacherId(),
                                    topSub.getTeacherName(),
                                    reviewerEmail != null ? reviewerEmail : "System Auto-Scheduler"
                            );
                            triggeredSubstitutions.add(sub);
                        }
                    }
                } catch (Exception ex) {
                    log.warn("Auto-substitution lookup notice for teacher {} on date {}: {}", req.getEmployeeName(), current, ex.getMessage());
                }
                current = current.plusDays(1);
            }
        }

        auditLogService.logAction(reviewerEmail != null ? reviewerEmail : "PRINCIPAL",
                approve ? "APPROVE_STAFF_LEAVE" : "REJECT_STAFF_LEAVE",
                "staff.staff_leave_requests", saved.getId().toString(),
                (approve ? "Approved" : "Rejected") + " leave for " + req.getEmployeeName() + " (" + triggeredSubstitutions.size() + " substitutions generated)");

        return new StaffLeaveReviewResult(saved, triggeredSubstitutions);
    }

    public record StaffLeaveReviewResult(StaffLeaveRequest leaveRequest, List<TeacherSubstitution> generatedSubstitutions) {}
}

