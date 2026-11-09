package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.BiometricPunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BiometricPunchRepository extends JpaRepository<BiometricPunch, UUID> {
    List<BiometricPunch> findByTenantIdOrderByPunchTimeDesc(String tenantId);
    List<BiometricPunch> findByTenantIdAndEmployeeIdOrderByPunchTimeDesc(String tenantId, UUID employeeId);
}

