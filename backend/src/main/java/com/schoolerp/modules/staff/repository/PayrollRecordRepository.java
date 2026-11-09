package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, UUID> {
    List<PayrollRecord> findByTenantIdAndPayrollMonthOrderByEmployeeNameAsc(String tenantId, String payrollMonth);
    Optional<PayrollRecord> findByTenantIdAndEmployeeIdAndPayrollMonth(String tenantId, UUID employeeId, String payrollMonth);
    Optional<PayrollRecord> findByTenantIdAndPayslipNumber(String tenantId, String payslipNumber);
    List<PayrollRecord> findByTenantId(String tenantId);
}

