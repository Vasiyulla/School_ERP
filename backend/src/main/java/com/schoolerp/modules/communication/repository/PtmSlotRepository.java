package com.schoolerp.modules.communication.repository;

import com.schoolerp.modules.communication.entity.PtmSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PtmSlotRepository extends JpaRepository<PtmSlot, UUID> {
    List<PtmSlot> findAllByTenantId(String tenantId);
    List<PtmSlot> findAllByTenantIdAndSessionId(String tenantId, UUID sessionId);
    List<PtmSlot> findAllByTenantIdAndSessionIdAndTeacherId(String tenantId, UUID sessionId, UUID teacherId);
    List<PtmSlot> findAllByTenantIdAndStudentId(String tenantId, UUID studentId);
}

