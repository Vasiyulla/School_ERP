package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.StaffSalaryLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffSalaryLoanRepository extends JpaRepository<StaffSalaryLoan, UUID> {
    List<StaffSalaryLoan> findByTenantId(String tenantId);
    List<StaffSalaryLoan> findByTenantIdAndEmployeeId(String tenantId, UUID employeeId);
    List<StaffSalaryLoan> findByTenantIdAndStatus(String tenantId, String status);
}

