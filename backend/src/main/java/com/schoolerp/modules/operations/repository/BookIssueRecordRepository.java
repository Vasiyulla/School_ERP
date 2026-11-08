package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.BookIssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookIssueRecordRepository extends JpaRepository<BookIssueRecord, UUID> {
    List<BookIssueRecord> findByTenantId(String tenantId);
    List<BookIssueRecord> findByTenantIdAndStudentId(String tenantId, UUID studentId);
    List<BookIssueRecord> findByTenantIdAndStatus(String tenantId, String status);
}

