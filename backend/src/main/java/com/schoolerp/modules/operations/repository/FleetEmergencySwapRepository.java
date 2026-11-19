package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.FleetEmergencySwap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FleetEmergencySwapRepository extends JpaRepository<FleetEmergencySwap, UUID> {
    List<FleetEmergencySwap> findAllByTenantIdAndStatus(String tenantId, String status);
    List<FleetEmergencySwap> findAllByTenantId(String tenantId);
    Optional<FleetEmergencySwap> findByIdAndTenantId(UUID id, String tenantId);
    long countByTenantId(String tenantId);
}
