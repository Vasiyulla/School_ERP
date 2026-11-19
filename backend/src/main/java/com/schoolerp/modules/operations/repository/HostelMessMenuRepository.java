package com.schoolerp.modules.operations.repository;

import com.schoolerp.modules.operations.entity.HostelMessMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HostelMessMenuRepository extends JpaRepository<HostelMessMenu, UUID> {
    List<HostelMessMenu> findAllByTenantIdAndDayOfWeek(String tenantId, String dayOfWeek);
    List<HostelMessMenu> findAllByTenantId(String tenantId);
    Optional<HostelMessMenu> findByTenantIdAndDayOfWeekAndMealType(String tenantId, String dayOfWeek, String mealType);
}
