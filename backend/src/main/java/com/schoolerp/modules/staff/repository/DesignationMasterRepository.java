package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.DesignationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignationMasterRepository extends JpaRepository<DesignationMaster, UUID> {
    List<DesignationMaster> findAllByTenantId(String tenantId);
    List<DesignationMaster> findAllByTenantIdAndIsActiveTrue(String tenantId);
    List<DesignationMaster> findAllByTenantIdAndDepartment(String tenantId, String department);
    List<DesignationMaster> findAllByTenantIdAndDepartmentAndIsActiveTrue(String tenantId, String department);
    Optional<DesignationMaster> findByTenantIdAndDesignationCode(String tenantId, String designationCode);
    Optional<DesignationMaster> findByIdAndTenantId(UUID id, String tenantId);
}

