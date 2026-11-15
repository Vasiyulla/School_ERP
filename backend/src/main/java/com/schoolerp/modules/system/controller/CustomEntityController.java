package com.schoolerp.modules.system.controller;

import com.schoolerp.common.response.ApiResponse;
import com.schoolerp.modules.system.dto.*;
import com.schoolerp.modules.system.service.CustomEntityExtensibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/system")
@RequiredArgsConstructor
public class CustomEntityController {

    private final CustomEntityExtensibilityService extensibilityService;

    // ==========================================
    // 1. CUSTOM FIELDS / METAFIELDS
    // ==========================================

    @PostMapping("/custom-fields")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<CustomFieldDefinitionDTO>> createCustomField(
            @RequestBody CreateCustomFieldDefinitionRequest request) {
        CustomFieldDefinitionDTO def = extensibilityService.createFieldDefinition(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Dynamic custom field defined successfully", def));
    }

    @GetMapping("/custom-fields")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<CustomFieldDefinitionDTO>>> listCustomFields(
            @RequestParam(required = false) String targetEntityType) {
        List<CustomFieldDefinitionDTO> list = extensibilityService.listFieldDefinitions(targetEntityType);
        return ResponseEntity.ok(ApiResponse.ok("Custom field definitions retrieved successfully", list));
    }

    @PostMapping("/custom-fields/values")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<String>> saveCustomFieldValues(
            @RequestBody SaveCustomFieldValueRequest request) {
        extensibilityService.saveCustomFieldValues(request);
        return ResponseEntity.ok(ApiResponse.ok("Custom field values saved successfully", "VALUES_SAVED"));
    }

    @GetMapping("/custom-fields/values/{entityType}/{entityId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STUDENT', 'PARENT', 'STAFF')")
    public ResponseEntity<ApiResponse<EntityCustomFieldsResponseDTO>> getEntityCustomFields(
            @PathVariable String entityType,
            @PathVariable UUID entityId) {
        EntityCustomFieldsResponseDTO dto = extensibilityService.getEntityCustomFields(entityType, entityId);
        return ResponseEntity.ok(ApiResponse.ok("Entity custom field values and metadata retrieved", dto));
    }

    // ==========================================
    // 2. UNIVERSAL CUSTOM ENTITIES & RECORDS
    // ==========================================

    @PostMapping("/custom-entities")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<CustomEntityDefinitionDTO>> createCustomEntity(
            @RequestBody CreateCustomEntityDefinitionRequest request) {
        CustomEntityDefinitionDTO def = extensibilityService.createEntityDefinition(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Universal custom entity defined successfully", def));
    }

    @GetMapping("/custom-entities")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<CustomEntityDefinitionDTO>>> listCustomEntities(
            @RequestParam(required = false) String category) {
        List<CustomEntityDefinitionDTO> list = extensibilityService.listEntityDefinitions(category);
        return ResponseEntity.ok(ApiResponse.ok("Tenant custom entities retrieved successfully", list));
    }

    @GetMapping("/custom-entities/{entityCode}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<CustomEntityDefinitionDTO>> getCustomEntity(
            @PathVariable String entityCode) {
        CustomEntityDefinitionDTO def = extensibilityService.getEntityDefinition(entityCode);
        return ResponseEntity.ok(ApiResponse.ok("Custom entity definition retrieved", def));
    }

    @PostMapping("/custom-entities/{entityCode}/records")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<CustomEntityRecordDTO>> createEntityRecord(
            @PathVariable String entityCode,
            @RequestBody CreateCustomEntityRecordRequest request,
            Authentication auth) {
        String createdBy = (auth != null) ? auth.getName() : "Administrator";
        CustomEntityRecordDTO record = extensibilityService.createEntityRecord(entityCode, request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Custom entity record created successfully", record));
    }

    @GetMapping("/custom-entities/{entityCode}/records")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<List<CustomEntityRecordDTO>>> listEntityRecords(
            @PathVariable String entityCode,
            @RequestParam(required = false) String status) {
        List<CustomEntityRecordDTO> list = extensibilityService.listEntityRecords(entityCode, status);
        return ResponseEntity.ok(ApiResponse.ok("Custom entity records retrieved successfully", list));
    }

    @GetMapping("/custom-entities/{entityCode}/records/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<CustomEntityRecordDTO>> getEntityRecord(
            @PathVariable String entityCode,
            @PathVariable UUID id) {
        CustomEntityRecordDTO record = extensibilityService.getEntityRecord(entityCode, id);
        return ResponseEntity.ok(ApiResponse.ok("Custom entity record detail retrieved", record));
    }

    @PutMapping("/custom-entities/{entityCode}/records/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL', 'TEACHER', 'STAFF')")
    public ResponseEntity<ApiResponse<CustomEntityRecordDTO>> updateEntityRecord(
            @PathVariable String entityCode,
            @PathVariable UUID id,
            @RequestBody UpdateCustomEntityRecordRequest request) {
        CustomEntityRecordDTO updated = extensibilityService.updateEntityRecord(entityCode, id, request);
        return ResponseEntity.ok(ApiResponse.ok("Custom entity record updated successfully", updated));
    }

    @DeleteMapping("/custom-entities/{entityCode}/records/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'PRINCIPAL')")
    public ResponseEntity<ApiResponse<String>> deleteEntityRecord(
            @PathVariable String entityCode,
            @PathVariable UUID id) {
        extensibilityService.deleteEntityRecord(entityCode, id);
        return ResponseEntity.ok(ApiResponse.ok("Custom entity record archived successfully", "RECORD_ARCHIVED"));
    }

    // ==========================================
    // 3. OUTBOUND WEBHOOKS
    // ==========================================

    @PostMapping("/webhooks")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<TenantWebhookSubscriptionDTO>> createWebhook(
            @RequestBody CreateWebhookSubscriptionRequest request) {
        TenantWebhookSubscriptionDTO sub = extensibilityService.createWebhook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tenant outbound webhook registered successfully", sub));
    }

    @GetMapping("/webhooks")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<TenantWebhookSubscriptionDTO>>> listWebhooks() {
        List<TenantWebhookSubscriptionDTO> list = extensibilityService.listWebhooks();
        return ResponseEntity.ok(ApiResponse.ok("Tenant webhooks retrieved successfully", list));
    }

    @PostMapping("/webhooks/{id}/test-ping")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<WebhookDispatchResultDTO>> testPingWebhook(
            @PathVariable UUID id) {
        WebhookDispatchResultDTO result = extensibilityService.testPingWebhook(id);
        return ResponseEntity.ok(ApiResponse.ok("Webhook test ping dispatched successfully", result));
    }
}

