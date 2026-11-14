package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.LearningPortfolioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LearningPortfolioItemRepository extends JpaRepository<LearningPortfolioItem, UUID> {
    List<LearningPortfolioItem> findByStudentIdOrderByCreatedAtDesc(UUID studentId);
    List<LearningPortfolioItem> findByTenantIdAndClassNameOrderByCreatedAtDesc(String tenantId, String className);
    List<LearningPortfolioItem> findByTenantIdAndIsFeaturedShowcaseTrueOrderByCreatedAtDesc(String tenantId);
}

