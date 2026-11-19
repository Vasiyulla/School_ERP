package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.DriverPreTripCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverPreTripCheckRepository extends JpaRepository<DriverPreTripCheck, UUID> {
    Optional<DriverPreTripCheck> findByTenantIdAndCheckDateAndVehicleRegistration(String tenantId, LocalDate checkDate, String vehicleRegistration);
    List<DriverPreTripCheck> findAllByTenantIdAndCheckDate(String tenantId, LocalDate checkDate);
}
