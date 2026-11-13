package com.schoolerp.modules.operations.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.operations.dto.ClaimLostFoundRequest;
import com.schoolerp.modules.operations.dto.LogLostFoundRequest;
import com.schoolerp.modules.operations.entity.LostAndFoundItem;
import com.schoolerp.modules.operations.service.LostAndFoundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operations/lost-and-found")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Campus Lost & Found Property Vault", description = "Register found property, browse catalog, and record verified student/parent chain-of-custody claims")
public class LostAndFoundController {

    private final LostAndFoundService lostAndFoundService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Log a found property item with location and storage locker reference")
    public ResponseEntity<ApiResponse<LostAndFoundItem>> logItem(
            @Valid @RequestBody LogLostFoundRequest request,
            Authentication auth) {
        String staff = (auth != null) ? auth.getName() : "Campus Duty Staff";
        LostAndFoundItem item = lostAndFoundService.logFoundItem(request, staff);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Lost & found item logged successfully", item));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL', 'STUDENT', 'PARENT')")
    @Operation(summary = "Browse lost & found items by status and category")
    public ResponseEntity<ApiResponse<List<LostAndFoundItem>>> getItems(
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false, defaultValue = "ALL") String category) {
        List<LostAndFoundItem> items = lostAndFoundService.getItems(status, category);
        return ResponseEntity.ok(ApiResponse.ok(items));
    }

    @PutMapping("/{itemId}/claim")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @Operation(summary = "Process verified student/parent property claim and handover")
    public ResponseEntity<ApiResponse<LostAndFoundItem>> claimItem(
            @PathVariable UUID itemId,
            @Valid @RequestBody ClaimLostFoundRequest request) {
        LostAndFoundItem claimed = lostAndFoundService.processClaim(itemId, request);
        return ResponseEntity.ok(ApiResponse.ok("Property handed over and claim recorded", claimed));
    }
}

