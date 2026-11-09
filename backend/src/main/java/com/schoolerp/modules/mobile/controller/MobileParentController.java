package com.schoolerp.modules.mobile.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.mobile.dto.ParentHomeDashboardDTO;
import com.schoolerp.modules.mobile.service.MobileParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mobile/parent")
@RequiredArgsConstructor
public class MobileParentController {

    private final MobileParentService mobileParentService;

    @GetMapping("/home")
    public ResponseEntity<ApiResponse<ParentHomeDashboardDTO>> getParentHome(
            @RequestParam(required = false) String guardianEmail) {
        ParentHomeDashboardDTO dashboard = mobileParentService.getParentDashboard(guardianEmail);
        return ResponseEntity.ok(ApiResponse.ok("Mobile parent home dashboard aggregated successfully", dashboard));
    }
}

