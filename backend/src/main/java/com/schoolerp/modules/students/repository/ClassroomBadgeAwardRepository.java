package com.schoolerp.modules.students.repository;

import com.schoolerp.modules.students.entity.ClassroomBadgeAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassroomBadgeAwardRepository extends JpaRepository<ClassroomBadgeAward, UUID> {
    List<ClassroomBadgeAward> findByStudentIdOrderByAwardedAtDesc(UUID studentId);
    List<ClassroomBadgeAward> findByTenantIdAndClassNameAndSectionNameOrderByAwardedAtDesc(String tenantId, String className, String sectionName);
}

