package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.VehicleComplianceSentinel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleComplianceSentinelRepository extends JpaRepository<VehicleComplianceSentinel, UUID> {
    Optional<VehicleComplianceSentinel> findByTenantIdAndVehicleId(String tenantId, UUID vehicleId);
    List<VehicleComplianceSentinel> findByTenantId(String tenantId);
}

