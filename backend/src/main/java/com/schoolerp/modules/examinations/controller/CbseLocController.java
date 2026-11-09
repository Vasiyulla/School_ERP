package com.schoolerp.modules.examinations.controller;

import com.schoolerp.common.annotation.RequireFeature;
import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.examinations.entity.CbseCandidateLoc;
import com.schoolerp.modules.examinations.service.CbseLocService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/examinations/cbse-loc")
@RequiredArgsConstructor
public class CbseLocController {

    private final CbseLocService locService;

    @PostMapping("/candidates")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','EXAM_CONTROLLER')")
    @RequireFeature("examinations.cbse_loc")
    public ResponseEntity<ApiResponse<CbseCandidateLoc>> registerCandidate(@RequestBody RegisterCandidateRequest request) {
        CbseCandidateLoc loc = locService.registerCandidate(
                request.getStudentId(),
                request.getMotherName(),
                request.getFatherName(),
                request.getClassStandard(),
                request.getStream(),
                request.getSubjectCodes(),
                request.getSubjectNames(),
                request.isBasicMath(),
                request.isCwsnScribeRequired()
        );
        return ResponseEntity.ok(ApiResponse.success(loc, "CBSE LOC Candidate registered and validated successfully"));
    }

    @GetMapping("/candidates")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','TEACHER','EXAM_CONTROLLER')")
    @RequireFeature("examinations.cbse_loc")
    public ResponseEntity<ApiResponse<List<CbseCandidateLoc>>> getCandidates(
            @RequestParam(required = false, defaultValue = "Class 10") String classStandard) {
        return ResponseEntity.ok(ApiResponse.success(locService.getCandidates(classStandard), "CBSE LOC Candidate roster retrieved"));
    }

    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRINCIPAL','EXAM_CONTROLLER')")
    @RequireFeature("examinations.cbse_loc")
    public ResponseEntity<ApiResponse<CbseLocService.CbseLocSubmissionReport>> exportLocSubmission(
            @RequestParam(required = false, defaultValue = "Class 10") String classStandard) {
        return ResponseEntity.ok(ApiResponse.success(locService.generateLocExport(classStandard), "Official CBSE LOC Pariksha Sangam report compiled"));
    }

    @Data
    public static class RegisterCandidateRequest {
        private UUID studentId;
        private String motherName;
        private String fatherName;
        private String classStandard;
        private String stream;
        private String subjectCodes;
        private String subjectNames;
        private boolean isBasicMath;
        private boolean cwsnScribeRequired;
    }
}

