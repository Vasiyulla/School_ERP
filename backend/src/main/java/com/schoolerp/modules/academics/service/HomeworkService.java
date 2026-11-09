package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.entity.HomeworkAssignment;
import com.schoolerp.modules.academics.entity.HomeworkSubmission;
import com.schoolerp.modules.academics.repository.HomeworkAssignmentRepository;
import com.schoolerp.modules.academics.repository.HomeworkSubmissionRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.system.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkAssignmentRepository assignmentRepository;
    private final HomeworkSubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public HomeworkAssignment createAssignment(String className, String sectionName, String subject,
                                               String title, String instructions, String teacherName,
                                               LocalDate dueDate, double maxMarks, String attachmentUrl) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        HomeworkAssignment assignment = HomeworkAssignment.builder()
                .tenantId(activeTenantId)
                .className(className)
                .sectionName(sectionName)
                .subject(subject)
                .title(title)
                .instructions(instructions)
                .teacherName(teacherName != null ? teacherName : "Faculty In-Charge")
                .assignedDate(LocalDate.now())
                .dueDate(dueDate != null ? dueDate : LocalDate.now().plusDays(3))
                .maxMarks(maxMarks > 0 ? maxMarks : 20.0)
                .attachmentUrl(attachmentUrl)
                .createdAt(LocalDateTime.now())
                .build();

        HomeworkAssignment saved = assignmentRepository.save(assignment);

        auditLogService.recordEvent(
                "CREATE_HOMEWORK_ASSIGNMENT",
                "academics.homework",
                saved.getId().toString(),
                String.format("Assigned homework '%s' for %s-%s by %s. Due on %s",
                        title, className, sectionName, teacherName, assignment.getDueDate())
        );

        log.info("Homework assigned: '{}' for {}-{}", title, className, sectionName);
        return saved;
    }

    @Transactional
    public HomeworkSubmission submitHomework(UUID assignmentId, UUID studentId, String submissionUrl) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        HomeworkAssignment assignment = assignmentRepository.findById(assignmentId)
                .filter(a -> a.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found: " + assignmentId));

        Student student = studentRepository.findById(studentId)
                .filter(s -> s.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        String status = LocalDate.now().isAfter(assignment.getDueDate()) ? "LATE" : "SUBMITTED";

        HomeworkSubmission sub = submissionRepository.findByTenantIdAndAssignmentIdAndStudentId(activeTenantId, assignmentId, studentId)
                .orElse(HomeworkSubmission.builder()
                        .tenantId(activeTenantId)
                        .assignmentId(assignmentId)
                        .studentId(studentId)
                        .studentName(student.getFullName())
                        .build());

        sub.setSubmittedAt(LocalDateTime.now());
        sub.setStatus(status);
        sub.setSubmissionUrl(submissionUrl != null ? submissionUrl : "https://cdn.apexacademy.edu.in/submissions/" + UUID.randomUUID() + ".pdf");

        HomeworkSubmission saved = submissionRepository.save(sub);

        auditLogService.recordEvent(
                "SUBMIT_HOMEWORK",
                "academics.homework_submissions",
                saved.getId().toString(),
                String.format("Student %s submitted assignment '%s' (%s): Status=%s",
                        student.getFullName(), assignment.getTitle(), assignment.getSubject(), status)
        );

        return saved;
    }

    @Transactional
    public HomeworkSubmission gradeSubmission(UUID submissionId, double marksAwarded, String feedback, String gradedBy) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";

        HomeworkSubmission sub = submissionRepository.findById(submissionId)
                .filter(s -> s.getTenantId().equals(activeTenantId))
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));

        sub.setMarksAwarded(marksAwarded);
        sub.setTeacherFeedback(feedback);
        sub.setGradedAt(LocalDateTime.now());
        sub.setGradedByTeacherName(gradedBy != null ? gradedBy : "Faculty In-Charge");
        sub.setStatus("GRADED");

        HomeworkSubmission saved = submissionRepository.save(sub);

        auditLogService.recordEvent(
                "GRADE_HOMEWORK",
                "academics.homework_submissions",
                saved.getId().toString(),
                String.format("Graded submission for %s: Marks=%.1f, Grader=%s, Feedback='%s'",
                        sub.getStudentName(), marksAwarded, gradedBy, feedback)
        );

        return saved;
    }

    @Transactional(readOnly = true)
    public List<HomeworkAssignment> getAssignmentsForClass(String className, String sectionName) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        if (sectionName != null && !sectionName.isBlank()) {
            return assignmentRepository.findAllByTenantIdAndClassNameAndSectionName(activeTenantId, className, sectionName);
        }
        return assignmentRepository.findAllByTenantIdAndClassName(activeTenantId, className);
    }

    @Transactional(readOnly = true)
    public List<HomeworkSubmission> getSubmissionsForAssignment(UUID assignmentId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return submissionRepository.findAllByTenantIdAndAssignmentId(activeTenantId, assignmentId);
    }

    @Transactional(readOnly = true)
    public List<HomeworkSubmission> getStudentSubmissions(UUID studentId) {
        final String activeTenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : "tenant_apex_mumbai_01";
        return submissionRepository.findAllByTenantIdAndStudentId(activeTenantId, studentId);
    }
}

