package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByTenantId(String tenantId);
    List<Employee> findAllByTenantId(String tenantId);
    Optional<Employee> findByIdAndTenantId(UUID id, String tenantId);
    long countByTenantId(String tenantId);
    Optional<Employee> findByTenantIdAndEmployeeCode(String tenantId, String employeeCode);
    Optional<Employee> findByTenantIdAndEmail(String tenantId, String email);
    List<Employee> findByTenantIdAndDepartment(String tenantId, String department);
}

