package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.LostAndFoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LostAndFoundItemRepository extends JpaRepository<LostAndFoundItem, UUID> {

    List<LostAndFoundItem> findAllByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<LostAndFoundItem> findAllByTenantIdAndStatusOrderByCreatedAtDesc(String tenantId, String status);

    List<LostAndFoundItem> findAllByTenantIdAndItemCategoryOrderByCreatedAtDesc(String tenantId, String itemCategory);

    Optional<LostAndFoundItem> findByIdAndTenantId(UUID id, String tenantId);

    long countByTenantId(String tenantId);
}

