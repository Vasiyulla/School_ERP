package com.schoolerp.modules.academics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.*;
import com.schoolerp.modules.academics.entity.ExitTicketResponse;
import com.schoolerp.modules.academics.entity.ExitTicketSession;
import com.schoolerp.modules.academics.repository.ExitTicketResponseRepository;
import com.schoolerp.modules.academics.repository.ExitTicketSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExitTicketService {

    private final ExitTicketSessionRepository sessionRepository;
    private final ExitTicketResponseRepository responseRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ExitTicketSessionDTO launchSession(LaunchExitTicketRequest request, String teacherId, String teacherName) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        String pin = "ET" + (1000 + new Random().nextInt(9000));

        String optionsJson = "[]";
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            try {
                optionsJson = objectMapper.writeValueAsString(request.getOptions());
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize options list to JSON: {}", e.getMessage());
            }
        }

        ExitTicketSession session = ExitTicketSession.builder()
                .tenantId(tenantId)
                .teacherId(teacherId)
                .teacherName(teacherName)
                .className(request.getClassName())
                .sectionName(request.getSectionName())
                .subjectName(request.getSubjectName())
                .topicName(request.getTopicName())
                .sessionPin(pin)
                .questionText(request.getQuestionText())
                .questionType(request.getQuestionType() != null ? request.getQuestionType() : "MCQ")
                .optionsJson(optionsJson)
                .correctOption(request.getCorrectOption())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        ExitTicketSession saved = sessionRepository.save(session);
        log.info("Launched Exit Ticket Session: {} (PIN: {}) for {} by {}", saved.getId(), pin, saved.getSubjectName(), teacherName);

        return toDto(saved);
    }

    @Transactional
    public String submitResponse(SubmitExitTicketResponseRequest request) {
        ExitTicketSession session;
        if (request.getSessionId() != null) {
            session = sessionRepository.findById(request.getSessionId())
                    .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + request.getSessionId()));
        } else if (request.getSessionPin() != null) {
            session = sessionRepository.findBySessionPinAndStatus(request.getSessionPin(), "ACTIVE")
                    .orElseThrow(() -> new IllegalArgumentException("No active session found with PIN: " + request.getSessionPin()));
        } else {
            throw new IllegalArgumentException("Either sessionId or sessionPin must be provided");
        }

        if (!"ACTIVE".equalsIgnoreCase(session.getStatus())) {
            throw new IllegalStateException("Exit ticket session is already closed.");
        }

        Boolean isCorrect = null;
        if (session.getCorrectOption() != null && request.getSelectedAnswer() != null) {
            isCorrect = session.getCorrectOption().equalsIgnoreCase(request.getSelectedAnswer().trim());
        }

        // Save or update response
        Optional<ExitTicketResponse> existing = responseRepository.findBySessionIdAndStudentId(session.getId(), request.getStudentId());
        ExitTicketResponse response = existing.orElseGet(() -> ExitTicketResponse.builder()
                .sessionId(session.getId())
                .studentId(request.getStudentId())
                .build());

        response.setStudentName(request.getStudentName());
        response.setSelectedAnswer(request.getSelectedAnswer());
        response.setIsCorrect(isCorrect);
        response.setConfidenceScore(Math.max(1, Math.min(3, request.getConfidenceScore())));
        response.setStudentNotes(request.getStudentNotes());
        response.setSubmittedAt(LocalDateTime.now());

        responseRepository.save(response);

        // Update session counters
        updateSessionMetrics(session);

        return "RESPONSE_SUBMITTED";
    }

    @Transactional(readOnly = true)
    public ExitTicketHeatmapDTO getHeatmap(UUID sessionId) {
        ExitTicketSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));

        List<ExitTicketResponse> responses = responseRepository.findBySessionIdOrderBySubmittedAtAsc(sessionId);

        List<String> struggling = responses.stream()
                .filter(r -> r.getConfidenceScore() == 1 || Boolean.FALSE.equals(r.getIsCorrect()))
                .map(ExitTicketResponse::getStudentName)
                .collect(Collectors.toList());

        String level = "EXCELLENT";
        if (session.getComprehensionRate() < 50.0) {
            level = "NEEDS_RETEACHING";
        } else if (session.getComprehensionRate() < 80.0) {
            level = "MODERATE";
        }

        return ExitTicketHeatmapDTO.builder()
                .sessionId(session.getId())
                .sessionPin(session.getSessionPin())
                .subjectName(session.getSubjectName())
                .topicName(session.getTopicName())
                .questionText(session.getQuestionText())
                .status(session.getStatus())
                .totalResponses(session.getTotalResponses())
                .understoodCount(session.getUnderstoodCount())
                .unsureCount(session.getUnsureCount())
                .strugglingCount(session.getStrugglingCount())
                .comprehensionRatePercentage(session.getComprehensionRate())
                .comprehensionLevel(level)
                .strugglingStudentNames(struggling)
                .build();
    }

    @Transactional
    public ExitTicketSessionDTO closeSession(UUID sessionId) {
        ExitTicketSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));

        session.setStatus("CLOSED");
        session.setClosedAt(LocalDateTime.now());
        updateSessionMetrics(session);

        ExitTicketSession saved = sessionRepository.save(session);
        return toDto(saved);
    }

    private void updateSessionMetrics(ExitTicketSession session) {
        List<ExitTicketResponse> responses = responseRepository.findBySessionIdOrderBySubmittedAtAsc(session.getId());
        int total = responses.size();
        int understood = (int) responses.stream().filter(r -> r.getConfidenceScore() == 3).count();
        int unsure = (int) responses.stream().filter(r -> r.getConfidenceScore() == 2).count();
        int struggling = (int) responses.stream().filter(r -> r.getConfidenceScore() == 1).count();

        double rate = total > 0 ? (double) understood / total * 100.0 : 0.0;

        session.setTotalResponses(total);
        session.setUnderstoodCount(understood);
        session.setUnsureCount(unsure);
        session.setStrugglingCount(struggling);
        session.setComprehensionRate(Math.round(rate * 10.0) / 10.0);

        sessionRepository.save(session);
    }

    private ExitTicketSessionDTO toDto(ExitTicketSession s) {
        return ExitTicketSessionDTO.builder()
                .id(s.getId())
                .teacherName(s.getTeacherName())
                .className(s.getClassName())
                .sectionName(s.getSectionName())
                .subjectName(s.getSubjectName())
                .topicName(s.getTopicName())
                .sessionPin(s.getSessionPin())
                .status(s.getStatus())
                .totalResponses(s.getTotalResponses())
                .comprehensionRate(s.getComprehensionRate())
                .createdAt(s.getCreatedAt())
                .build();
    }
}

