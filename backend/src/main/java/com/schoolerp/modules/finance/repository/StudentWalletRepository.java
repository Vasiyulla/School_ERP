package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.StudentWallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentWalletRepository extends JpaRepository<StudentWallet, UUID> {
    List<StudentWallet> findAllByTenantId(String tenantId);
    Optional<StudentWallet> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    Optional<StudentWallet> findByTenantIdAndRfidCardNumber(String tenantId, String rfidCardNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM StudentWallet w WHERE w.tenantId = :tenantId AND w.rfidCardNumber = :rfidCardNumber")
    Optional<StudentWallet> findByTenantIdAndRfidCardNumberForUpdate(@Param("tenantId") String tenantId, @Param("rfidCardNumber") String rfidCardNumber);
}

