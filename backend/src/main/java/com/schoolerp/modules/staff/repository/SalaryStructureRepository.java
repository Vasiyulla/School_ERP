package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, UUID> {
    Optional<SalaryStructure> findByTenantIdAndEmployeeIdAndStatus(String tenantId, UUID employeeId, String status);
}

