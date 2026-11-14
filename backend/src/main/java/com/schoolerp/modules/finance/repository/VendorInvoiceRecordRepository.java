package com.schoolerp.modules.finance.repository;

import com.schoolerp.modules.finance.entity.VendorInvoiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VendorInvoiceRecordRepository extends JpaRepository<VendorInvoiceRecord, UUID> {
    List<VendorInvoiceRecord> findByTenantId(String tenantId);
    List<VendorInvoiceRecord> findByTenantIdAndPaymentStatus(String tenantId, String paymentStatus);
}

