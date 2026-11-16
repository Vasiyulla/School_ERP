package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.EventCostumeRentalDTO;
import com.schoolerp.modules.operations.dto.IssueCostumeRequest;
import com.schoolerp.modules.operations.dto.RegisterCostumeRequest;
import com.schoolerp.modules.operations.service.CostumeRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operations/events/costumes")
@RequiredArgsConstructor
public class EventCostumeController {

    private final CostumeRentalService costumeRentalService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<EventCostumeRentalDTO>> registerCostume(
            @RequestBody RegisterCostumeRequest request) {
        EventCostumeRentalDTO costume = costumeRentalService.registerCostume(request);
        return ResponseEntity.ok(ApiResponse.ok("Event theatrical costume registered in wardrobe inventory", costume));
    }

    @PutMapping("/issue-and-return")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<EventCostumeRentalDTO>> issueOrReturnCostume(
            @RequestBody IssueCostumeRequest request) {
        EventCostumeRentalDTO costume = costumeRentalService.issueOrReturnCostume(request);
        return ResponseEntity.ok(ApiResponse.ok("Event costume rental status updated", costume));
    }

    @GetMapping("/roster")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<EventCostumeRentalDTO>>> getCostumeRoster(
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) String status) {
        List<EventCostumeRentalDTO> roster = costumeRentalService.getRoster(eventName, status);
        return ResponseEntity.ok(ApiResponse.ok("Event costume wardrobe roster retrieved", roster));
    }
}
