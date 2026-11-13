package com.schoolerp.modules.examinations.repository;

import com.schoolerp.modules.examinations.entity.QuestionBankItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionBankItemRepository extends JpaRepository<QuestionBankItem, UUID> {

    List<QuestionBankItem> findAllByTenantIdAndIsActiveTrue(String tenantId);

    List<QuestionBankItem> findAllByTenantIdAndSubjectNameAndGradeLevelAndIsActiveTrue(
            String tenantId, String subjectName, String gradeLevel);

    List<QuestionBankItem> findAllByTenantIdAndSubjectNameAndGradeLevelAndQuestionTypeAndIsActiveTrue(
            String tenantId, String subjectName, String gradeLevel, String questionType);

    Optional<QuestionBankItem> findByIdAndTenantId(UUID id, String tenantId);
}

