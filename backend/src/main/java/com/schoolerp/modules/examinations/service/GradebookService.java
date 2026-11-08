package com.schoolerp.modules.examinations.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.examinations.entity.StudentMark;
import com.schoolerp.modules.examinations.repository.StudentMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradebookService {

    private final StudentMarkRepository markRepository;

    public static String calculateGradeBand(int total) {
        if (total >= 91) return "A1";
        if (total >= 81) return "A2";
        if (total >= 71) return "B1";
        if (total >= 61) return "B2";
        if (total >= 51) return "C1";
        if (total >= 41) return "C2";
        if (total >= 33) return "D";
        return "E";
    }

    @Transactional(readOnly = true)
    public List<StudentMark> getMarks(String termName, String subject) {
        return markRepository.findAllByTenantIdAndTermNameAndSubject(
                TenantContext.getTenantId(), termName, subject);
    }

    @Transactional
    public StudentMark saveMark(String termName, UUID studentId, String studentName, String subject,
                                int theory, int practical, int internal, String remarks) {
        String tenantId = TenantContext.getTenantId();

        // Bounds validation
        theory = Math.min(80, Math.max(0, theory));
        practical = Math.min(20, Math.max(0, practical));
        internal = Math.min(10, Math.max(0, internal));

        // Normalized score out of 100%
        int rawTotal = theory + practical + internal;
        int normalized = Math.round(((float) rawTotal / 110.0f) * 100.0f);
        String gradeBand = calculateGradeBand(normalized);

        int finalTheory = theory;
        int finalPractical = practical;
        int finalInternal = internal;

        StudentMark mark = markRepository
                .findByTenantIdAndTermNameAndStudentIdAndSubject(tenantId, termName, studentId, subject)
                .orElseGet(() -> StudentMark.builder()
                        .tenantId(tenantId)
                        .termName(termName)
                        .studentId(studentId)
                        .studentName(studentName)
                        .subject(subject)
                        .build());

        mark.setTheoryScore(finalTheory);
        mark.setPracticalScore(finalPractical);
        mark.setInternalScore(finalInternal);
        mark.setTotalScore(normalized);
        mark.setGradeBand(gradeBand);
        mark.setRemarks(remarks);

        return markRepository.save(mark);
    }

    @Transactional
    public List<StudentMark> lockGradebook(String termName, String subject) {
        List<StudentMark> marks = getMarks(termName, subject);
        marks.forEach(m -> m.setStatus("LOCKED"));
        return markRepository.saveAll(marks);
    }
}

