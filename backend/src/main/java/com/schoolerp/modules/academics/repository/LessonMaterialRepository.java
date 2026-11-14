package com.schoolerp.modules.academics.repository;

import com.schoolerp.modules.academics.entity.LessonMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonMaterialRepository extends JpaRepository<LessonMaterial, UUID> {
    List<LessonMaterial> findAllByTenantId(String tenantId);
    List<LessonMaterial> findAllByTenantIdAndClassName(String tenantId, String className);
    List<LessonMaterial> findAllByTenantIdAndClassNameAndSubjectName(String tenantId, String className, String subjectName);
    List<LessonMaterial> findAllByTenantIdAndTeacherName(String tenantId, String teacherName);
    Optional<LessonMaterial> findByIdAndTenantId(UUID id, String tenantId);
}

