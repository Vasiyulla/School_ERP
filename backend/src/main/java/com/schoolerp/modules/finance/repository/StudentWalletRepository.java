package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.StudentWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentWalletRepository extends JpaRepository<StudentWallet, UUID> {
    List<StudentWallet> findAllByTenantId(String tenantId);
    Optional<StudentWallet> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<StudentWallet> findByTenantIdAndRfidCardNumber(String tenantId, String rfidCardNumber);
}

