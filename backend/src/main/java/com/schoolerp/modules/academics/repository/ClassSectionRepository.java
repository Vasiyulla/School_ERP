package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.ClassSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassSectionRepository extends JpaRepository<ClassSection, UUID> {
    List<ClassSection> findAllByTenantId(String tenantId);
    Optional<ClassSection> findByTenantIdAndClassNameAndSectionName(String tenantId, String className, String sectionName);
}

