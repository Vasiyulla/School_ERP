package com.schoolerp.modules.staff.repository;

import com.schoolerp.modules.staff.entity.StaffTaxDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffTaxDeclarationRepository extends JpaRepository<StaffTaxDeclaration, UUID> {
    List<StaffTaxDeclaration> findByTenantId(String tenantId);
    Optional<StaffTaxDeclaration> findByTenantIdAndEmployeeIdAndFinancialYear(String tenantId, UUID employeeId, String financialYear);
    Optional<StaffTaxDeclaration> findByTenantIdAndEmployeeId(String tenantId, UUID employeeId);
}

