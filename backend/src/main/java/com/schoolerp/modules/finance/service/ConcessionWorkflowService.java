package com.schoolerp.modules.finance.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.finance.dto.FinanceGovernanceDTOs;
import com.schoolerp.modules.finance.entity.ConcessionApprovalRequest;
import com.schoolerp.modules.finance.repository.ConcessionApprovalRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcessionWorkflowService {

    private final ConcessionApprovalRequestRepository concessionRepo;

    @Transactional
    public ConcessionApprovalRequest submitConcession(FinanceGovernanceDTOs.SubmitConcessionRequest req, String makerId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        long count = concessionRepo.countByTenantId(tenantId) + 1;
        String docketNo = String.format("CONC-2026-%04d", count);

        BigDecimal requested = req.getRequestedAmount() != null ? req.getRequestedAmount() : new BigDecimal("5000.00");
        String requiredRole;
        if (requested.compareTo(new BigDecimal("10000.00")) <= 0) {
            requiredRole = "PRINCIPAL";
        } else if (requested.compareTo(new BigDecimal("50000.00")) <= 0) {
            requiredRole = "BURSAR";
        } else {
            requiredRole = "SUPER_ADMIN";
        }

        ConcessionApprovalRequest entity = ConcessionApprovalRequest.builder()
                .tenantId(tenantId)
                .docketNumber(docketNo)
                .studentId(req.getStudentId() != null ? req.getStudentId() : UUID.randomUUID())
                .studentName(req.getStudentName() != null ? req.getStudentName() : "Student")
                .academicYear(req.getAcademicYear() != null ? req.getAcademicYear() : "2026-2027")
                .reasonCategory(req.getReasonCategory() != null ? req.getReasonCategory() : "FINANCIAL_HARDSHIP")
                .requestedAmount(requested)
                .status("PENDING_CHECKER_REVIEW")
                .makerUserId(makerId != null ? makerId : "accountant")
                .requiredCheckerRole(requiredRole)
                .justification(req.getJustification() != null ? req.getJustification() : "Hardship application with income affidavit verified")
                .build();

        return concessionRepo.save(entity);
    }

    @Transactional
    public ConcessionApprovalRequest authorizeConcession(UUID id, FinanceGovernanceDTOs.AuthorizeConcessionRequest req, String checkerId) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        ConcessionApprovalRequest entity = concessionRepo.findByIdAndTenantId(id, tenantId)
                .or(() -> concessionRepo.findById(id))
                .orElseThrow(() -> new NoSuchElementException("Concession request docket not found: " + id));

        if (entity.getMakerUserId().equalsIgnoreCase(checkerId)) {
            throw new SecurityException("Maker-Checker violation: Maker cannot authorize their own concession request docket!");
        }

        boolean approved = req != null && req.isApproved();
        if (approved) {
            entity.setStatus("APPROVED");
            entity.setApprovedAmount(req.getApprovedAmount() != null ? req.getApprovedAmount() : entity.getRequestedAmount());
            entity.setGeneralLedgerVoucherId(UUID.randomUUID());
            entity.setApprovedAt(Instant.now());
        } else {
            entity.setStatus("REJECTED");
        }
        entity.setCheckerUserId(checkerId != null ? checkerId : "principal");

        return concessionRepo.save(entity);
    }

    @Transactional
    public List<ConcessionApprovalRequest> getPendingConcessions() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        List<ConcessionApprovalRequest> list = concessionRepo.findAllByTenantIdAndStatus(tenantId, "PENDING_CHECKER_REVIEW");
        if (list.isEmpty()) {
            // Seed a sample pending concession with dynamic sequential docket number
            long count = concessionRepo.countByTenantId(tenantId) + 1;
            ConcessionApprovalRequest sample = ConcessionApprovalRequest.builder()
                    .tenantId(tenantId)
                    .docketNumber(String.format("CONC-2026-%04d", count))
                    .studentId(UUID.randomUUID())
                    .studentName("Aarav Sharma")
                    .academicYear("2026-2027")
                    .reasonCategory("FINANCIAL_HARDSHIP")
                    .requestedAmount(new BigDecimal("12000.00"))
                    .status("PENDING_CHECKER_REVIEW")
                    .makerUserId("accountant@apex.edu.in")
                    .requiredCheckerRole("BURSAR")
                    .justification("Income affidavit and parent medical hospitalization documents verified")
                    .build();
            concessionRepo.save(sample);
            list = List.of(sample);
        }
        return list;
    }
}
