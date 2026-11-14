package com.schoolerp.modules.institution.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.institution.dto.*;
import com.schoolerp.modules.institution.entity.EmergencyCampusMode;
import com.schoolerp.modules.institution.entity.EmergencySafetyCheckIn;
import com.schoolerp.modules.institution.repository.EmergencyCampusModeRepository;
import com.schoolerp.modules.institution.repository.EmergencySafetyCheckInRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmergencyCampusModeService {

    private final EmergencyCampusModeRepository modeRepository;
    private final EmergencySafetyCheckInRepository safetyRepository;

    @Transactional
    public EmergencyCampusModeDTO declareEmergencyMode(DeclareEmergencyModeRequest request, String declaredBy) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        boolean freeze = "COMPLETE_EMERGENCY_SHUTDOWN".equalsIgnoreCase(request.getOperatingMode())
                || Boolean.TRUE.equals(request.getFreezeLateFeesAndDeadlines());

        EmergencyCampusMode mode = EmergencyCampusMode.builder()
                .tenantId(tenantId)
                .operatingMode(request.getOperatingMode())
                .emergencyReason(request.getEmergencyReason())
                .declaredByStaffName(declaredBy)
                .effectiveDate(request.getEffectiveDate() != null ? request.getEffectiveDate() : LocalDate.now())
                .endDate(request.getEndDate())
                .autoNotifyParents(request.getAutoNotifyParents() != null ? request.getAutoNotifyParents() : true)
                .suspendTransportAlerts(request.getSuspendTransportAlerts() != null ? request.getSuspendTransportAlerts() : true)
                .freezeLateFeesAndDeadlines(freeze)
                .govtOrderReference(request.getGovtOrderReference())
                .closureNoticeBulletin(request.getClosureNoticeBulletin())
                .activeVirtualRoomsCount("VIRTUAL_EMERGENCY_CAMPUS".equalsIgnoreCase(request.getOperatingMode()) ? 8 : 0)
                .declaredAt(LocalDateTime.now())
                .build();

        EmergencyCampusMode saved = modeRepository.save(mode);
        log.warn("EMERGENCY CAMPUS MODE DECLARED: [{}] for reason '{}' by {}. FreezeDeadlines: {}",
                saved.getOperatingMode(), saved.getEmergencyReason(), declaredBy, freeze);

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public EmergencyCampusModeDTO getCurrentStatus() {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        return modeRepository.findTopByTenantIdOrderByDeclaredAtDesc(tenantId)
                .map(this::toDto)
                .orElseGet(() -> EmergencyCampusModeDTO.builder()
                        .operatingMode("PHYSICAL_CAMPUS")
                        .emergencyReason("NONE_NORMAL_OPERATIONS")
                        .effectiveDate(LocalDate.now())
                        .autoNotifyParents(false)
                        .suspendTransportAlerts(false)
                        .freezeLateFeesAndDeadlines(false)
                        .activeVirtualRoomsCount(0)
                        .build());
    }

    @Transactional
    public EmergencyCampusModeDTO restorePhysicalCampus(String restoredBy) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        EmergencyCampusMode restored = EmergencyCampusMode.builder()
                .tenantId(tenantId)
                .operatingMode("PHYSICAL_CAMPUS")
                .emergencyReason("ALL_CLEAR_NORMAL_RESUMED")
                .declaredByStaffName(restoredBy)
                .effectiveDate(LocalDate.now())
                .autoNotifyParents(true)
                .suspendTransportAlerts(false)
                .freezeLateFeesAndDeadlines(false)
                .closureNoticeBulletin("Normal physical campus operations restored. Transport and cafeteria active.")
                .activeVirtualRoomsCount(0)
                .declaredAt(LocalDateTime.now())
                .build();

        EmergencyCampusMode saved = modeRepository.save(restored);
        log.info("Physical campus restored by {}", restoredBy);
        return toDto(saved);
    }

    @Transactional
    public EmergencySafetyCheckInDTO submitSafetyCheckIn(SubmitSafetyCheckInRequest request) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        EmergencySafetyCheckIn checkIn = EmergencySafetyCheckIn.builder()
                .tenantId(tenantId)
                .userId(request.getUserId())
                .userName(request.getUserName())
                .userRole(request.getUserRole())
                .safetyStatus(request.getSafetyStatus())
                .currentLocation(request.getCurrentLocation())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .assistanceDetails(request.getAssistanceDetails())
                .reportedAt(LocalDateTime.now())
                .build();

        EmergencySafetyCheckIn saved = safetyRepository.save(checkIn);
        log.info("Safety check-in logged for {} ({}): Status = {}", request.getUserName(), request.getUserRole(), request.getSafetyStatus());

        return toSafetyDto(saved);
    }

    @Transactional(readOnly = true)
    public SafetyCheckInSummaryDTO getSafetyCheckInSummary() {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        List<EmergencySafetyCheckIn> all = safetyRepository.findByTenantIdOrderByReportedAtDesc(tenantId);

        int safe = (int) all.stream().filter(c -> "SAFE_AT_HOME".equalsIgnoreCase(c.getSafetyStatus())).count();
        int evacuated = (int) all.stream().filter(c -> "EVACUATED_TO_SHELTER".equalsIgnoreCase(c.getSafetyStatus())).count();
        int urgent = (int) all.stream().filter(c -> "NEED_URGENT_ASSISTANCE".equalsIgnoreCase(c.getSafetyStatus())).count();
        int medical = (int) all.stream().filter(c -> "MEDICAL_EMERGENCY".equalsIgnoreCase(c.getSafetyStatus())).count();

        List<EmergencySafetyCheckInDTO> urgentCases = all.stream()
                .filter(c -> "NEED_URGENT_ASSISTANCE".equalsIgnoreCase(c.getSafetyStatus()) || "MEDICAL_EMERGENCY".equalsIgnoreCase(c.getSafetyStatus()))
                .map(this::toSafetyDto)
                .collect(Collectors.toList());

        return SafetyCheckInSummaryDTO.builder()
                .tenantId(tenantId)
                .totalCheckIns(all.size())
                .safeAtHomeCount(safe)
                .evacuatedToShelterCount(evacuated)
                .needUrgentAssistanceCount(urgent)
                .medicalEmergencyCount(medical)
                .urgentCases(urgentCases)
                .build();
    }

    private EmergencyCampusModeDTO toDto(EmergencyCampusMode m) {
        return EmergencyCampusModeDTO.builder()
                .id(m.getId())
                .operatingMode(m.getOperatingMode())
                .emergencyReason(m.getEmergencyReason())
                .declaredByStaffName(m.getDeclaredByStaffName())
                .effectiveDate(m.getEffectiveDate())
                .endDate(m.getEndDate())
                .autoNotifyParents(m.isAutoNotifyParents())
                .suspendTransportAlerts(m.isSuspendTransportAlerts())
                .freezeLateFeesAndDeadlines(m.isFreezeLateFeesAndDeadlines())
                .govtOrderReference(m.getGovtOrderReference())
                .closureNoticeBulletin(m.getClosureNoticeBulletin())
                .activeVirtualRoomsCount(m.getActiveVirtualRoomsCount())
                .declaredAt(m.getDeclaredAt())
                .build();
    }

    private EmergencySafetyCheckInDTO toSafetyDto(EmergencySafetyCheckIn s) {
        return EmergencySafetyCheckInDTO.builder()
                .id(s.getId())
                .userId(s.getUserId())
                .userName(s.getUserName())
                .userRole(s.getUserRole())
                .safetyStatus(s.getSafetyStatus())
                .currentLocation(s.getCurrentLocation())
                .emergencyContactPhone(s.getEmergencyContactPhone())
                .assistanceDetails(s.getAssistanceDetails())
                .reportedAt(s.getReportedAt())
                .build();
    }
}

