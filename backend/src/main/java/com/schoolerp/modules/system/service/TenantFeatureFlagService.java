package com.schoolerp.modules.system.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.system.entity.TenantFeatureFlag;
import com.schoolerp.modules.system.repository.TenantFeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantFeatureFlagService {

    private final TenantFeatureFlagRepository featureFlagRepository;
    private final AuditLogService auditLogService;

    @Cacheable(value = "tenantFeatures", key = "#featureKey")
    public boolean isFeatureEnabled(String featureKey) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return featureFlagRepository.findByTenantIdAndFeatureKey(tenantId, featureKey)
                .map(TenantFeatureFlag::isEnabled)
                .orElse(true); // Default enabled unless explicitly restricted
    }

    @Transactional
    @CacheEvict(value = "tenantFeatures", allEntries = true)
    public TenantFeatureFlag toggleFeature(String featureKey, boolean enabled, String tier) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        TenantFeatureFlag flag = featureFlagRepository.findByTenantIdAndFeatureKey(tenantId, featureKey)
                .orElseGet(() -> TenantFeatureFlag.builder()
                        .tenantId(tenantId)
                        .featureKey(featureKey)
                        .featureName(formatFeatureName(featureKey))
                        .category(extractCategory(featureKey))
                        .build());

        flag.setEnabled(enabled);
        if (tier != null) {
            flag.setTier(tier.toUpperCase());
        }
        flag.setUpdatedAt(Instant.now());

        TenantFeatureFlag saved = featureFlagRepository.save(flag);
        auditLogService.recordEvent(
                "TOGGLE_FEATURE_FLAG",
                "system.tenant_feature_flags",
                saved.getFeatureKey(),
                String.format("Feature flag %s set to enabled=%s (Tier: %s)", saved.getFeatureKey(), enabled, saved.getTier())
        );

        return saved;
    }

    public List<TenantFeatureFlag> getAllFeatures() {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        List<TenantFeatureFlag> flags = featureFlagRepository.findByTenantId(tenantId);
        if (flags.isEmpty()) {
            seedDefaultFeatures(tenantId);
            return featureFlagRepository.findByTenantId(tenantId);
        }
        return flags;
    }

    @Transactional
    public void seedDefaultFeatures(String tenantId) {
        List<TenantFeatureFlag> defaults = List.of(
                TenantFeatureFlag.builder().tenantId(tenantId).featureKey("FINANCE.INVOICING").featureName("Tuition Invoicing & Receipts").category("FINANCE").enabled(true).tier("STARTER").build(),
                TenantFeatureFlag.builder().tenantId(tenantId).featureKey("FINANCE.RFID_WALLET").featureName("Cashless RFID Student Canteen Wallet").category("FINANCE").enabled(true).tier("ENTERPRISE").build(),
                TenantFeatureFlag.builder().tenantId(tenantId).featureKey("ATTENDANCE.BIOMETRIC").featureName("Biometric Turnstile Hardware Sync").category("ATTENDANCE").enabled(true).tier("ENTERPRISE").build(),
                TenantFeatureFlag.builder().tenantId(tenantId).featureKey("COMMUNICATION.WHATSAPP").featureName("Meta WhatsApp Cloud API Broadcasts").category("COMMUNICATION").enabled(true).tier("GROWTH").build(),
                TenantFeatureFlag.builder().tenantId(tenantId).featureKey("EXAMS.CBSE_70_20_10").featureName("CBSE 70/20/10 Weighted Gradebook").category("EXAMINATIONS").enabled(true).tier("GROWTH").build(),
                TenantFeatureFlag.builder().tenantId(tenantId).featureKey("OPERATIONS.FLEET_GPS").featureName("Samsara IoT Bus GPS Telematics").category("OPERATIONS").enabled(true).tier("GROWTH").build()
        );

        for (TenantFeatureFlag f : defaults) {
            if (!featureFlagRepository.existsByTenantIdAndFeatureKey(tenantId, f.getFeatureKey())) {
                featureFlagRepository.save(f);
            }
        }
    }

    private String formatFeatureName(String key) {
        String[] parts = key.split("\\.");
        String name = parts.length > 1 ? parts[1] : parts[0];
        return name.replace('_', ' ');
    }

    private String extractCategory(String key) {
        return key.contains(".") ? key.split("\\.")[0] : "GENERAL";
    }
}

