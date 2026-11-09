package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.operations.entity.GatePass;
import com.schoolerp.modules.operations.repository.GatePassRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatePassService {

    private final GatePassRepository gatePassRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public GatePass issueGatePass(UUID studentId, String studentName, String className,
                                  String guardianName, String guardianPhone, String guardianRelationship,
                                  String reason, String approvedByStaffName) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        long count = gatePassRepository.countByTenantId(activeTenantId);
        String passNumber = String.format("GP-%s-%04d",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")), count + 1);
        String qrToken = "qr_gp_" + UUID.randomUUID().toString().replace("-", "");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validUntil = now.plusHours(3); // 3-hour security departure window

        GatePass pass = GatePass.builder()
                .tenantId(activeTenantId)
                .passNumber(passNumber)
                .studentId(studentId)
                .studentName(studentName)
                .className(className)
                .guardianName(guardianName)
                .guardianPhone(guardianPhone)
                .guardianRelationship(guardianRelationship != null ? guardianRelationship : "PARENT")
                .reason(reason != null ? reason : "PARENT_PICKUP")
                .passStatus("APPROVED")
                .approvedByStaffName(approvedByStaffName != null ? approvedByStaffName : "Vice Principal Desk")
                .issuedAt(now)
                .validUntil(validUntil)
                .qrToken(qrToken)
                .build();

        GatePass saved = gatePassRepository.save(pass);

        auditLogService.recordEvent(
                "ISSUE_GATE_PASS",
                "operations.gate_passes",
                pass.getId().toString(),
                String.format("Issued Gate Pass %s for student %s (%s) picked up by %s (%s). Valid until %s",
                        passNumber, studentName, className, guardianName, guardianRelationship, validUntil)
        );

        log.info("Gate pass issued: {} for student: {}", passNumber, studentName);
        return saved;
    }

    @Transactional
    public GatePass verifyAndScanGatePass(String qrToken, String securityGuardName) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        GatePass pass = gatePassRepository.findByTenantIdAndQrToken(activeTenantId, qrToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or unrecognized gate pass QR token: " + qrToken));

        if (!"APPROVED".equalsIgnoreCase(pass.getPassStatus())) {
            throw new IllegalStateException("Gate pass " + pass.getPassNumber() + " cannot be scanned. Current status: " + pass.getPassStatus());
        }

        if (LocalDateTime.now().isAfter(pass.getValidUntil())) {
            pass.setPassStatus("EXPIRED");
            gatePassRepository.save(pass);
            throw new IllegalStateException("Gate pass " + pass.getPassNumber() + " has expired at " + pass.getValidUntil());
        }

        pass.setPassStatus("SCANNED_EXIT");
        pass.setExitTimestamp(LocalDateTime.now());
        pass.setSecurityGuardName(securityGuardName != null ? securityGuardName : "Main Gate Security Desk");

        GatePass scanned = gatePassRepository.save(pass);

        auditLogService.recordEvent(
                "SCAN_GATE_PASS_EXIT",
                "operations.gate_passes",
                pass.getId().toString(),
                String.format("Gate Pass %s scanned at main gate. Student %s departed with %s (%s) at %s verified by %s",
                        pass.getPassNumber(), pass.getStudentName(), pass.getGuardianName(),
                        pass.getGuardianRelationship(), pass.getExitTimestamp(), pass.getSecurityGuardName())
        );

        log.info("Gate pass scanned successfully: {} at {}", pass.getPassNumber(), pass.getExitTimestamp());
        return scanned;
    }

    @Transactional(readOnly = true)
    public List<GatePass> getActiveGatePasses() {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return gatePassRepository.findAllByTenantIdAndPassStatus(activeTenantId, "APPROVED");
    }

    @Transactional(readOnly = true)
    public List<GatePass> getAllGatePasses() {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return gatePassRepository.findAllByTenantId(activeTenantId);
    }
}

