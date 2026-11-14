package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.CautionMoneyDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CautionMoneyDepositRepository extends JpaRepository<CautionMoneyDeposit, UUID> {
    List<CautionMoneyDeposit> findByTenantId(String tenantId);
    Optional<CautionMoneyDeposit> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<CautionMoneyDeposit> findByTenantIdAndAdmissionNumber(String tenantId, String admissionNumber);
}

