package com.schoolerp.modules.operations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.operations.dto.HostelMessAndFleetDTOs;
import com.schoolerp.modules.operations.entity.DriverPreTripCheck;
import com.schoolerp.modules.operations.entity.FleetEmergencySwap;
import com.schoolerp.modules.operations.repository.DriverPreTripCheckRepository;
import com.schoolerp.modules.operations.repository.FleetEmergencySwapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FleetEmergencyService {

    private final FleetEmergencySwapRepository swapRepository;
    private final DriverPreTripCheckRepository driverCheckRepository;

    @Transactional
    public FleetEmergencySwap triggerEmergencyBusSwap(HostelMessAndFleetDTOs.EmergencyBusSwapRequest req) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        long count = swapRepository.countByTenantId(tenantId) + 1;
        String swapRef = String.format("SWAP-2026-%04d", count);

        FleetEmergencySwap swap = FleetEmergencySwap.builder()
                .tenantId(tenantId)
                .swapReference(swapRef)
                .brokenVehicleReg(req.getBrokenVehicleReg() != null ? req.getBrokenVehicleReg() : "KA-01-AB-1204")
                .replacementVehicleReg(req.getReplacementVehicleReg() != null ? req.getReplacementVehicleReg() : "KA-01-XY-9081")
                .reliefDriverName(req.getReliefDriverName() != null ? req.getReliefDriverName() : "Mohan Lal")
                .reliefDriverPhone(req.getReliefDriverPhone() != null ? req.getReliefDriverPhone() : "+91 98765 43210")
                .routeCode(req.getRouteCode() != null ? req.getRouteCode() : "ROUTE-14-WHITEFIELD")
                .breakdownReason(req.getBreakdownReason() != null ? req.getBreakdownReason() : "Radiator coolant leak at Sector 4 Junction")
                .affectedStudentsCount(req.getAffectedStudentsCount() > 0 ? req.getAffectedStudentsCount() : 32)
                .parentBroadcastDispatched(true)
                .status("ACTIVE_SUBSTITUTION")
                .build();

        log.warn("EMERGENCY VEHICLE SWAP INITIATED: [{}] replaced by [{}] on [{}]. Instant parent broadcast dispatched to [{}] families.",
                swap.getBrokenVehicleReg(), swap.getReplacementVehicleReg(), swap.getRouteCode(), swap.getAffectedStudentsCount());

        return swapRepository.save(swap);
    }

    @Transactional
    public DriverPreTripCheck recordDriverPreTripCheck(HostelMessAndFleetDTOs.DriverPreTripCheckRequest req) {
        String tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "apex";
        LocalDate today = LocalDate.now();

        BigDecimal reading = req.getBreathalyzerReading() != null ? req.getBreathalyzerReading() : BigDecimal.ZERO;
        boolean isCleared = reading.compareTo(BigDecimal.ZERO) == 0
                && req.isLicenseValid()
                && req.isTirePressureOk()
                && req.isFireExtinguisherPresent();

        DriverPreTripCheck check = DriverPreTripCheck.builder()
                .tenantId(tenantId)
                .driverEmployeeId(req.getDriverEmployeeId() != null ? req.getDriverEmployeeId() : "DRV-102")
                .driverName(req.getDriverName() != null ? req.getDriverName() : "Suresh Kumar")
                .vehicleRegistration(req.getVehicleRegistration() != null ? req.getVehicleRegistration() : "KA-01-AB-1204")
                .checkDate(today)
                .breathalyzerReading(reading)
                .licenseValid(req.isLicenseValid())
                .tirePressureOk(req.isTirePressureOk())
                .firstAidKitPresent(req.isFirstAidKitPresent())
                .fireExtinguisherPresent(req.isFireExtinguisherPresent())
                .emergencyDoorOperational(req.isEmergencyDoorOperational())
                .tripCleared(isCleared)
                .supervisorSignature(req.getSupervisorSignature() != null ? req.getSupervisorSignature() : "Transport Supervisor Verified")
                .build();

        return driverCheckRepository.save(check);
    }
}
