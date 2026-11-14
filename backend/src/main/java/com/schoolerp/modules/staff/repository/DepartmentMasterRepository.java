package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.DepartmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentMasterRepository extends JpaRepository<DepartmentMaster, UUID> {
    List<DepartmentMaster> findAllByTenantId(String tenantId);
    List<DepartmentMaster> findAllByTenantIdAndIsActiveTrue(String tenantId);
    Optional<DepartmentMaster> findByTenantIdAndDepartmentCode(String tenantId, String departmentCode);
}

