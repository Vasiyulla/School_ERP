package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.VehicleFuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleFuelLogRepository extends JpaRepository<VehicleFuelLog, UUID> {
    List<VehicleFuelLog> findByTenantIdAndVehicleIdOrderByFillDateDesc(String tenantId, UUID vehicleId);
    List<VehicleFuelLog> findByTenantIdOrderByFillDateDesc(String tenantId);
}

