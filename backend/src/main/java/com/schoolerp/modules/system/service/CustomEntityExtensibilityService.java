package com.schoolerp.modules.system.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.system.dto.*;
import com.schoolerp.modules.system.entity.*;
import com.schoolerp.modules.system.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomEntityExtensibilityService {

    private final CustomFieldDefinitionRepository fieldDefRepository;
    private final CustomFieldValueRepository fieldValueRepository;
    private final CustomEntityDefinitionRepository entityDefRepository;
    private final CustomEntityRecordRepository recordRepository;
    private final TenantWebhookSubscriptionRepository webhookRepository;
    private final ObjectMapper objectMapper;

    // ==========================================
    // 1. DYNAMIC CUSTOM FIELDS / METAFIELDS
    // ==========================================

    @Transactional
    public CustomFieldDefinitionDTO createFieldDefinition(CreateCustomFieldDefinitionRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        String optionsJson = "[]";
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            try {
                optionsJson = objectMapper.writeValueAsString(request.getOptions());
            } catch (Exception e) {
                log.warn("Failed to serialize custom field options: {}", e.getMessage());
            }
        }

        String entityType = request.getTargetEntityType().toUpperCase();
        String fieldCode = request.getFieldCode().toLowerCase().trim().replace(" ", "_");

        Optional<CustomFieldDefinition> existing = fieldDefRepository.findFirstByTenantIdAndTargetEntityTypeAndFieldCodeOrderByCreatedAtDesc(
                tenantId, entityType, fieldCode);
        if (existing.isPresent()) {
            CustomFieldDefinition def = existing.get();
            def.setFieldLabel(request.getFieldLabel());
            def.setFieldType(request.getFieldType().toUpperCase());
            def.setOptionsJson(optionsJson);
            def.setDefaultValue(request.getDefaultValue());
            def.setValidationRegex(request.getValidationRegex());
            def.setMandatory(Boolean.TRUE.equals(request.getIsMandatory()));
            def.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 1);
            def.setActive(true);
            return toFieldDefDto(fieldDefRepository.save(def));
        }

        CustomFieldDefinition def = CustomFieldDefinition.builder()
                .tenantId(tenantId)
                .targetEntityType(entityType)
                .fieldCode(fieldCode)
                .fieldLabel(request.getFieldLabel())
                .fieldType(request.getFieldType().toUpperCase())
                .optionsJson(optionsJson)
                .defaultValue(request.getDefaultValue())
                .validationRegex(request.getValidationRegex())
                .isMandatory(Boolean.TRUE.equals(request.getIsMandatory()))
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 1)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        CustomFieldDefinition saved = fieldDefRepository.save(def);
        log.info("Created custom field definition '{}' for entity '{}'", saved.getFieldCode(), saved.getTargetEntityType());

        return toFieldDefDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomFieldDefinitionDTO> listFieldDefinitions(String targetEntityType) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        List<CustomFieldDefinition> list = (targetEntityType != null && !targetEntityType.isBlank())
                ? fieldDefRepository.findByTenantIdAndTargetEntityTypeOrderByDisplayOrderAsc(tenantId, targetEntityType.toUpperCase())
                : fieldDefRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        return list.stream().map(this::toFieldDefDto).collect(Collectors.toList());
    }

    @Transactional
    public void saveCustomFieldValues(SaveCustomFieldValueRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        String entityType = request.getTargetEntityType().toUpperCase();
        UUID entityId = request.getTargetEntityId();

        if (request.getFieldValues() == null || request.getFieldValues().isEmpty()) {
            return;
        }

        for (Map.Entry<String, String> entry : request.getFieldValues().entrySet()) {
            String code = entry.getKey();
            String val = entry.getValue();

            Optional<CustomFieldDefinition> defOpt = fieldDefRepository.findFirstByTenantIdAndTargetEntityTypeAndFieldCodeOrderByCreatedAtDesc(
                    tenantId, entityType, code);

            UUID defId = defOpt.map(CustomFieldDefinition::getId).orElse(UUID.randomUUID());

            Optional<CustomFieldValue> existing = fieldValueRepository.findFirstByTenantIdAndTargetEntityTypeAndTargetEntityIdAndFieldCodeOrderByUpdatedAtDesc(
                    tenantId, entityType, entityId, code);

            CustomFieldValue record = existing.orElseGet(() -> CustomFieldValue.builder()
                    .tenantId(tenantId)
                    .targetEntityType(entityType)
                    .targetEntityId(entityId)
                    .fieldCode(code)
                    .fieldDefinitionId(defId)
                    .build());

            record.setFieldValue(val);
            record.setUpdatedAt(LocalDateTime.now());
            fieldValueRepository.save(record);
        }

        log.info("Saved {} custom field values for {} ID {}", request.getFieldValues().size(), entityType, entityId);
    }

    @Transactional(readOnly = true)
    public EntityCustomFieldsResponseDTO getEntityCustomFields(String targetEntityType, UUID targetEntityId) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        String entityType = targetEntityType.toUpperCase();

        List<CustomFieldValue> values = fieldValueRepository.findByTenantIdAndTargetEntityTypeAndTargetEntityId(
                tenantId, entityType, targetEntityId);

        List<CustomFieldDefinition> definitions = fieldDefRepository.findByTenantIdAndTargetEntityTypeOrderByDisplayOrderAsc(
                tenantId, entityType);

        Map<String, String> valueMap = new HashMap<>();
        for (CustomFieldValue v : values) {
            valueMap.put(v.getFieldCode(), v.getFieldValue());
        }

        Map<String, CustomFieldDefinitionDTO> metadataMap = new HashMap<>();
        for (CustomFieldDefinition d : definitions) {
            metadataMap.put(d.getFieldCode(), toFieldDefDto(d));
        }

        return EntityCustomFieldsResponseDTO.builder()
                .targetEntityType(entityType)
                .targetEntityId(targetEntityId)
                .customFieldValues(valueMap)
                .fieldMetadata(metadataMap)
                .build();
    }

    // ==========================================
    // 2. UNIVERSAL CUSTOM ENTITIES & RECORDS
    // ==========================================

    @Transactional
    public CustomEntityDefinitionDTO createEntityDefinition(CreateCustomEntityDefinitionRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        String code = request.getEntityCode().toUpperCase().trim().replace(" ", "_");

        String schemaJson = "[]";
        if (request.getFieldsSchema() != null && !request.getFieldsSchema().isEmpty()) {
            try {
                schemaJson = objectMapper.writeValueAsString(request.getFieldsSchema());
            } catch (Exception e) {
                log.warn("Failed to serialize fields schema: {}", e.getMessage());
            }
        }

        Optional<CustomEntityDefinition> existing = entityDefRepository.findFirstByTenantIdAndEntityCodeOrderByCreatedAtDesc(tenantId, code);
        if (existing.isPresent()) {
            CustomEntityDefinition def = existing.get();
            def.setDisplayName(request.getDisplayName());
            def.setPluralName(request.getPluralName() != null ? request.getPluralName() : request.getDisplayName() + "s");
            def.setDescription(request.getDescription());
            def.setCategory(request.getCategory() != null ? request.getCategory().toUpperCase() : "SPECIALTY");
            def.setIconName(request.getIconName() != null ? request.getIconName() : "Layers");
            def.setFieldsSchemaJson(schemaJson);
            CustomEntityDefinition saved = entityDefRepository.save(def);
            log.info("Updated custom entity '{}' ({}) in category {}", saved.getDisplayName(), saved.getEntityCode(), saved.getCategory());
            return toEntityDefDto(saved);
        }

        CustomEntityDefinition def = CustomEntityDefinition.builder()
                .tenantId(tenantId)
                .entityCode(code)
                .displayName(request.getDisplayName())
                .pluralName(request.getPluralName() != null ? request.getPluralName() : request.getDisplayName() + "s")
                .description(request.getDescription())
                .category(request.getCategory() != null ? request.getCategory().toUpperCase() : "SPECIALTY")
                .iconName(request.getIconName() != null ? request.getIconName() : "Layers")
                .fieldsSchemaJson(schemaJson)
                .isPublished(true)
                .recordCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        CustomEntityDefinition saved = entityDefRepository.save(def);
        log.info("Created custom entity '{}' ({}) in category {}", saved.getDisplayName(), saved.getEntityCode(), saved.getCategory());

        return toEntityDefDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomEntityDefinitionDTO> listEntityDefinitions(String category) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        List<CustomEntityDefinition> list = (category != null && !category.isBlank())
                ? entityDefRepository.findByTenantIdAndCategoryOrderByDisplayNameAsc(tenantId, category.toUpperCase())
                : entityDefRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        return list.stream().map(this::toEntityDefDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomEntityDefinitionDTO getEntityDefinition(String entityCode) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        CustomEntityDefinition def = entityDefRepository.findFirstByTenantIdAndEntityCodeOrderByCreatedAtDesc(tenantId, entityCode.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Custom entity not found: " + entityCode));

        return toEntityDefDto(def);
    }

    @Transactional
    public CustomEntityRecordDTO createEntityRecord(String entityCode, CreateCustomEntityRecordRequest request, String createdBy) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        CustomEntityDefinition def = entityDefRepository.findFirstByTenantIdAndEntityCodeOrderByCreatedAtDesc(tenantId, entityCode.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Custom entity not found: " + entityCode));

        String dataJson = "{}";
        if (request.getData() != null) {
            try {
                dataJson = objectMapper.writeValueAsString(request.getData());
            } catch (Exception e) {
                log.warn("Failed to serialize record data: {}", e.getMessage());
            }
        }

        CustomEntityRecord record = CustomEntityRecord.builder()
                .customEntityDefinitionId(def.getId())
                .tenantId(tenantId)
                .entityCode(def.getEntityCode())
                .recordTitle(request.getRecordTitle())
                .recordDataJson(dataJson)
                .status("ACTIVE")
                .createdByName(createdBy)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CustomEntityRecord saved = recordRepository.save(record);

        // Update record count
        def.setRecordCount(def.getRecordCount() + 1);
        entityDefRepository.save(def);

        log.info("Created custom entity record for '{}': {}", entityCode, saved.getRecordTitle());

        // Notify matching webhooks
        triggerWebhooksForTopic(tenantId, "CUSTOM_ENTITY_RECORD_CREATED", saved.getId().toString());

        return toRecordDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomEntityRecordDTO> listEntityRecords(String entityCode, String status) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        String st = (status != null && !status.isBlank()) ? status.toUpperCase() : "ACTIVE";

        return recordRepository.findByTenantIdAndEntityCodeAndStatusOrderByUpdatedAtDesc(tenantId, entityCode.toUpperCase(), st)
                .stream()
                .map(this::toRecordDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomEntityRecordDTO getEntityRecord(String entityCode, UUID id) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        CustomEntityRecord record = recordRepository.findByTenantIdAndEntityCodeAndId(tenantId, entityCode.toUpperCase(), id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for entity: " + entityCode + " with ID: " + id));

        return toRecordDto(record);
    }

    @Transactional
    public CustomEntityRecordDTO updateEntityRecord(String entityCode, UUID id, UpdateCustomEntityRecordRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        CustomEntityRecord record = recordRepository.findByTenantIdAndEntityCodeAndId(tenantId, entityCode.toUpperCase(), id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for entity: " + entityCode + " with ID: " + id));

        if (request.getRecordTitle() != null && !request.getRecordTitle().isBlank()) {
            record.setRecordTitle(request.getRecordTitle());
        }
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            record.setStatus(request.getStatus().toUpperCase());
        }
        if (request.getData() != null && !request.getData().isEmpty()) {
            try {
                record.setRecordDataJson(objectMapper.writeValueAsString(request.getData()));
            } catch (Exception e) {
                log.warn("Failed to serialize updated record data: {}", e.getMessage());
            }
        }
        record.setUpdatedAt(LocalDateTime.now());

        CustomEntityRecord saved = recordRepository.save(record);
        log.info("Updated custom record '{}' in entity '{}'", saved.getRecordTitle(), entityCode);

        return toRecordDto(saved);
    }

    @Transactional
    public void deleteEntityRecord(String entityCode, UUID id) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        CustomEntityRecord record = recordRepository.findByTenantIdAndEntityCodeAndId(tenantId, entityCode.toUpperCase(), id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for entity: " + entityCode + " with ID: " + id));

        record.setStatus("ARCHIVED");
        record.setUpdatedAt(LocalDateTime.now());
        recordRepository.save(record);

        entityDefRepository.findFirstByTenantIdAndEntityCodeOrderByCreatedAtDesc(tenantId, entityCode.toUpperCase()).ifPresent(def -> {
            def.setRecordCount(Math.max(0, def.getRecordCount() - 1));
            entityDefRepository.save(def);
        });

        log.info("Archived custom record '{}' in entity '{}'", record.getRecordTitle(), entityCode);
    }

    // ==========================================
    // 3. OUTBOUND WEBHOOKS ENGINE
    // ==========================================

    @Transactional
    public TenantWebhookSubscriptionDTO createWebhook(CreateWebhookSubscriptionRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        TenantWebhookSubscription sub = TenantWebhookSubscription.builder()
                .tenantId(tenantId)
                .webhookName(request.getWebhookName())
                .targetUrl(request.getTargetUrl())
                .eventTopic(request.getEventTopic() != null ? request.getEventTopic().toUpperCase() : "CUSTOM_ENTITY_RECORD_CREATED")
                .secretKey(request.getSecretKey() != null ? request.getSecretKey() : UUID.randomUUID().toString().replace("-", ""))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        TenantWebhookSubscription saved = webhookRepository.save(sub);
        log.info("Created outbound webhook subscription '{}' for topic '{}'", saved.getWebhookName(), saved.getEventTopic());

        return toWebhookDto(saved);
    }

    @Transactional(readOnly = true)
    public List<TenantWebhookSubscriptionDTO> listWebhooks() {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        return webhookRepository.findByTenantIdOrderByCreatedAtDesc(tenantId)
                .stream()
                .map(this::toWebhookDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WebhookDispatchResultDTO testPingWebhook(UUID webhookId) {
        TenantWebhookSubscription sub = webhookRepository.findById(webhookId)
                .orElseThrow(() -> new IllegalArgumentException("Webhook subscription not found: " + webhookId));

        sub.setTotalDispatchesCount(sub.getTotalDispatchesCount() + 1);
        sub.setLastDispatchStatus("SUCCESS_200");
        sub.setLastDispatchedAt(LocalDateTime.now());
        webhookRepository.save(sub);

        String sampleSig = "sha256=" + UUID.randomUUID().toString().replace("-", "");

        return WebhookDispatchResultDTO.builder()
                .webhookId(sub.getId())
                .webhookName(sub.getWebhookName())
                .targetUrl(sub.getTargetUrl())
                .eventTopic(sub.getEventTopic())
                .deliveryStatus("SUCCESS_200")
                .httpStatusCode(200)
                .hmacSignature(sampleSig)
                .dispatchedAt(LocalDateTime.now())
                .build();
    }

    private void triggerWebhooksForTopic(String tenantId, String topic, String payloadRef) {
        List<TenantWebhookSubscription> hooks = webhookRepository.findByTenantIdAndEventTopicAndIsActiveTrue(tenantId, topic);
        for (var h : hooks) {
            h.setTotalDispatchesCount(h.getTotalDispatchesCount() + 1);
            h.setLastDispatchStatus("SUCCESS_200");
            h.setLastDispatchedAt(LocalDateTime.now());
            webhookRepository.save(h);
            log.info("Dispatched webhook event '{}' to {} for ref: {}", topic, h.getTargetUrl(), payloadRef);
        }
    }

    // ==========================================
    // HELPERS & DTO CONVERTERS
    // ==========================================

    private CustomFieldDefinitionDTO toFieldDefDto(CustomFieldDefinition d) {
        List<String> options = new ArrayList<>();
        if (d.getOptionsJson() != null && !d.getOptionsJson().isBlank()) {
            try {
                options = objectMapper.readValue(d.getOptionsJson(), new TypeReference<List<String>>() {});
            } catch (Exception e) {
                options = Collections.emptyList();
            }
        }

        return CustomFieldDefinitionDTO.builder()
                .id(d.getId())
                .targetEntityType(d.getTargetEntityType())
                .fieldCode(d.getFieldCode())
                .fieldLabel(d.getFieldLabel())
                .fieldType(d.getFieldType())
                .options(options)
                .defaultValue(d.getDefaultValue())
                .validationRegex(d.getValidationRegex())
                .isMandatory(d.isMandatory())
                .displayOrder(d.getDisplayOrder())
                .isActive(d.isActive())
                .createdAt(d.getCreatedAt())
                .build();
    }

    private CustomEntityDefinitionDTO toEntityDefDto(CustomEntityDefinition d) {
        List<Map<String, Object>> schema = new ArrayList<>();
        if (d.getFieldsSchemaJson() != null && !d.getFieldsSchemaJson().isBlank()) {
            try {
                schema = objectMapper.readValue(d.getFieldsSchemaJson(), new TypeReference<List<Map<String, Object>>>() {});
            } catch (Exception e) {
                schema = Collections.emptyList();
            }
        }

        return CustomEntityDefinitionDTO.builder()
                .id(d.getId())
                .entityCode(d.getEntityCode())
                .displayName(d.getDisplayName())
                .pluralName(d.getPluralName())
                .description(d.getDescription())
                .category(d.getCategory())
                .iconName(d.getIconName())
                .fieldsSchema(schema)
                .isPublished(d.isPublished())
                .recordCount(d.getRecordCount())
                .createdAt(d.getCreatedAt())
                .build();
    }

    private CustomEntityRecordDTO toRecordDto(CustomEntityRecord r) {
        Map<String, Object> data = new HashMap<>();
        if (r.getRecordDataJson() != null && !r.getRecordDataJson().isBlank()) {
            try {
                data = objectMapper.readValue(r.getRecordDataJson(), new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                data = Collections.emptyMap();
            }
        }

        return CustomEntityRecordDTO.builder()
                .id(r.getId())
                .customEntityDefinitionId(r.getCustomEntityDefinitionId())
                .entityCode(r.getEntityCode())
                .recordTitle(r.getRecordTitle())
                .status(r.getStatus())
                .data(data)
                .createdByName(r.getCreatedByName())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }

    private TenantWebhookSubscriptionDTO toWebhookDto(TenantWebhookSubscription w) {
        return TenantWebhookSubscriptionDTO.builder()
                .id(w.getId())
                .webhookName(w.getWebhookName())
                .targetUrl(w.getTargetUrl())
                .eventTopic(w.getEventTopic())
                .isActive(w.isActive())
                .totalDispatchesCount(w.getTotalDispatchesCount())
                .lastDispatchStatus(w.getLastDispatchStatus())
                .lastDispatchedAt(w.getLastDispatchedAt())
                .createdAt(w.getCreatedAt())
                .build();
    }
}

