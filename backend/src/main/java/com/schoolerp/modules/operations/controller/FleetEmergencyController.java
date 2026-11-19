package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.HostelMessAndFleetDTOs;
import com.schoolerp.modules.operations.entity.DriverPreTripCheck;
import com.schoolerp.modules.operations.entity.FleetEmergencySwap;
import com.schoolerp.modules.operations.service.FleetEmergencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operations/fleet")
@RequiredArgsConstructor
public class FleetEmergencyController {

    private final FleetEmergencyService fleetEmergencyService;

    @PostMapping("/breakdown/emergency-swap")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TRANSPORT_MANAGER') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<FleetEmergencySwap>> triggerEmergencyBusSwap(
            @RequestBody HostelMessAndFleetDTOs.EmergencyBusSwapRequest request) {
        FleetEmergencySwap swap = fleetEmergencyService.triggerEmergencyBusSwap(request);
        return ResponseEntity.ok(ApiResponse.ok("Emergency bus substitution initiated & parent broadcast dispatched", swap));
    }

    @PostMapping("/driver/pre-trip-check")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('TRANSPORT_MANAGER') or hasRole('SECURITY_GUARD')")
    public ResponseEntity<ApiResponse<DriverPreTripCheck>> recordDriverPreTripCheck(
            @RequestBody HostelMessAndFleetDTOs.DriverPreTripCheckRequest request) {
        DriverPreTripCheck check = fleetEmergencyService.recordDriverPreTripCheck(request);
        return ResponseEntity.ok(ApiResponse.ok("Morning driver pre-trip safety checklist logged", check));
    }
}
