package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.HostelMessAndFleetDTOs;
import com.schoolerp.modules.operations.entity.HostelMessMenu;
import com.schoolerp.modules.operations.entity.HostelRoomInspection;
import com.schoolerp.modules.operations.service.HostelMessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/operations/hostel")
@RequiredArgsConstructor
public class HostelMessController {

    private final HostelMessService hostelMessService;

    @PostMapping("/mess/menu")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('HOSTEL_WARDEN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<HostelMessMenu>> configureMenu(
            @RequestBody HostelMessAndFleetDTOs.ConfigureMessMenuRequest request) {
        HostelMessMenu menu = hostelMessService.configureMenu(request);
        return ResponseEntity.ok(ApiResponse.ok("Hostel dining menu configured successfully", menu));
    }

    @GetMapping("/mess/menu/today")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<HostelMessAndFleetDTOs.DailyMessMenuOverviewDTO>> getTodayMenu() {
        HostelMessAndFleetDTOs.DailyMessMenuOverviewDTO overview = hostelMessService.getTodayMenu();
        return ResponseEntity.ok(ApiResponse.ok("Today's dining hall menu retrieved", overview));
    }

    @PostMapping("/mess/meal-punch")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('HOSTEL_WARDEN') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<HostelMessAndFleetDTOs.MealPunchResponseDTO>> recordMealPunch(
            @RequestBody HostelMessAndFleetDTOs.MealPunchRequest request) {
        HostelMessAndFleetDTOs.MealPunchResponseDTO response = hostelMessService.recordMealPunch(request);
        return ResponseEntity.ok(ApiResponse.ok(response.getMessage(), response));
    }

    @PostMapping("/inspections/checkout")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('HOSTEL_WARDEN') or hasRole('PRINCIPAL')")
    public ResponseEntity<ApiResponse<HostelRoomInspection>> recordRoomCheckout(
            @RequestBody HostelMessAndFleetDTOs.RoomInspectionRequest request) {
        HostelRoomInspection inspection = hostelMessService.recordRoomInspection(request);
        return ResponseEntity.ok(ApiResponse.ok("Hostel room checkout inspection recorded", inspection));
    }
}
