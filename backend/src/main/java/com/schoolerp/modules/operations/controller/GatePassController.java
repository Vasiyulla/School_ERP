package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.entity.GatePass;
import com.schoolerp.modules.operations.service.GatePassService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations/gate-passes")
@RequiredArgsConstructor
public class GatePassController {

    private final GatePassService gatePassService;

    @Data
    public static class IssueGatePassRequest {
        private UUID studentId;
        private String studentName;
        private String className;
        private String guardianName;
        private String guardianPhone;
        private String guardianRelationship;
        private String reason;
        private String approvedByStaffName;
    }

    @Data
    public static class VerifyGatePassRequest {
        private String qrToken;
        private String securityGuardName;
    }

    @PostMapping("/issue")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<GatePass>> issueGatePass(@RequestBody IssueGatePassRequest req) {
        GatePass pass = gatePassService.issueGatePass(
                req.getStudentId(),
                req.getStudentName(),
                req.getClassName(),
                req.getGuardianName(),
                req.getGuardianPhone(),
                req.getGuardianRelationship(),
                req.getReason(),
                req.getApprovedByStaffName()
        );
        return ResponseEntity.ok(ApiResponse.ok("Gate pass issued successfully", pass));
    }

    @PostMapping("/verify-scan")
    public ResponseEntity<ApiResponse<GatePass>> verifyAndScanGatePass(@RequestBody VerifyGatePassRequest req) {
        GatePass scanned = gatePassService.verifyAndScanGatePass(req.getQrToken(), req.getSecurityGuardName());
        return ResponseEntity.ok(ApiResponse.ok("Gate pass verified and departure stamped", scanned));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'STAFF')")
    public ResponseEntity<ApiResponse<List<GatePass>>> getActivePasses() {
        return ResponseEntity.ok(ApiResponse.ok(gatePassService.getActiveGatePasses()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<GatePass>>> getAllPasses() {
        return ResponseEntity.ok(ApiResponse.ok(gatePassService.getAllGatePasses()));
    }
}

