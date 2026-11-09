package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.ChequeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChequeRecordRepository extends JpaRepository<ChequeRecord, UUID> {
    List<ChequeRecord> findByTenantIdOrderByReceivedAtDesc(String tenantId);
    List<ChequeRecord> findByTenantIdAndStatus(String tenantId, String status);
    Optional<ChequeRecord> findByTenantIdAndChequeNumber(String tenantId, String chequeNumber);
}
