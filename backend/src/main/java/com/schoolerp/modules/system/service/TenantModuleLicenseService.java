package com.schoolerp.modules.system.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.system.entity.TenantModuleLicense;
import com.schoolerp.modules.system.repository.TenantModuleLicenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantModuleLicenseService {

    private final TenantModuleLicenseRepository licenseRepository;
    private final AuditLogService auditLogService;

    public List<TenantModuleLicense> getTenantModules() {
        return licenseRepository.findByTenantId(TenantContext.getTenantId());
    }

    public boolean isModuleEnabled(String tenantId, String moduleKey) {
        return licenseRepository.findByTenantIdAndModuleKey(tenantId, moduleKey)
                .map(TenantModuleLicense::isEnabled)
                .orElse(true); // Default to enabled if not explicitly restricted
    }

    @Transactional
    public TenantModuleLicense toggleModule(String moduleKey, boolean enabled, String actor) {
        String tenantId = TenantContext.getTenantId();
        TenantModuleLicense license = licenseRepository
                .findByTenantIdAndModuleKey(tenantId, moduleKey)
                .orElseThrow(() -> new IllegalArgumentException("Module key not recognized: " + moduleKey));

        license.setEnabled(enabled);
        license.setUpdatedBy(actor);
        license.setUpdatedAt(Instant.now());
        TenantModuleLicense saved = licenseRepository.save(license);

        auditLogService.recordEvent(
                "TOGGLE_MODULE_LICENSE",
                "system.tenant_modules",
                saved.getId().toString(),
                String.format("Module '%s' (%s) set to %s by %s",
                        saved.getModuleName(), saved.getModuleKey(), enabled ? "ENABLED" : "DISABLED", actor)
        );

        return saved;
    }

    @Transactional
    public void seedDefaultModulesForTenant(String tenantId) {
        if (!licenseRepository.findByTenantId(tenantId).isEmpty()) {
            return;
        }

        Instant defaultExpiry = Instant.now().plus(365, ChronoUnit.DAYS);

        List<TenantModuleLicense> defaultModules = List.of(
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("CORE_ACADEMICS").moduleName("Curriculum, Classes & Timetable").tier("STARTER").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("STUDENT_LIFECYCLE").moduleName("Student Master & Physical Locker Custody").tier("STARTER").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("ATTENDANCE").moduleName("Batch Roll Call & Bi-Temporal Audit Engine").tier("STARTER").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("EXAMINATIONS").moduleName("CBSE 70/20/10 Assessment & QR Report Cards").tier("PROFESSIONAL").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("FINANCE").moduleName("Fee Structures, Concessions, Cheques & Double-Entry Ledger").tier("ENTERPRISE").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("COMMUNICATION").moduleName("Circulars, WhatsApp/SMS & Mobile Push FCM").tier("ENTERPRISE").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("FLEET_GPS").moduleName("Live Bus Fleet GPS Telematics & Route Dispatch").tier("ENTERPRISE").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("LIBRARY").moduleName("Library Circulation & RFID Cataloging").tier("PROFESSIONAL").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build(),
                TenantModuleLicense.builder().tenantId(tenantId).moduleKey("ADMISSIONS").moduleName("Admissions Pipeline & Verification Desk").tier("PROFESSIONAL").isEnabled(true).expiresAt(defaultExpiry).updatedBy("SYSTEM").build()
        );

        licenseRepository.saveAll(defaultModules);
        log.info("Provisioned 9 enterprise dynamic module licenses for tenant {}", tenantId);
    }
}
