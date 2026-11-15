package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.HostelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HostelRoomRepository extends JpaRepository<HostelRoom, UUID> {
    List<HostelRoom> findByTenantIdAndBlockName(String tenantId, String blockName);
    List<HostelRoom> findByTenantId(String tenantId);
    Optional<HostelRoom> findByTenantIdAndBlockNameAndRoomNumber(String tenantId, String blockName, String roomNumber);
}

