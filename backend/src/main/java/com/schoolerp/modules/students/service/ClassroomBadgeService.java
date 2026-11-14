package com.schoolerp.modules.students.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.students.dto.*;
import com.schoolerp.modules.students.entity.ClassroomBadgeAward;
import com.schoolerp.modules.students.entity.SchoolHouse;
import com.schoolerp.modules.students.repository.ClassroomBadgeAwardRepository;
import com.schoolerp.modules.students.repository.SchoolHouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomBadgeService {

    private final ClassroomBadgeAwardRepository badgeRepository;
    private final SchoolHouseRepository houseRepository;

    @Transactional
    public ClassroomBadgeDTO awardBadge(AwardBadgeRequest request, String teacherName) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");

        String badgeTitle;
        String category;
        String emoji;
        int points;

        switch (request.getBadgeCode() != null ? request.getBadgeCode().toUpperCase() : "BRILLIANT_INSIGHT") {
            case "PEER_HELPER":
                badgeTitle = "Helping a Peer";
                category = "CITIZENSHIP";
                emoji = "🤝";
                points = 15;
                break;
            case "ACTIVE_PARTICIPANT":
                badgeTitle = "Active Class Participation";
                category = "ENGAGEMENT";
                emoji = "🎯";
                points = 10;
                break;
            case "DEEP_THINKER":
                badgeTitle = "Deep Conceptual Inquiry";
                category = "ACADEMIC";
                emoji = "🧠";
                points = 20;
                break;
            case "INTEGRITY_HONESTY":
                badgeTitle = "Exemplary Integrity & Honesty";
                category = "CHARACTER";
                emoji = "🌟";
                points = 25;
                break;
            case "LAB_SAFETY_CHAMPION":
                badgeTitle = "Lab Safety & Precision Champion";
                category = "PRACTICAL";
                emoji = "🔬";
                points = 15;
                break;
            case "BRILLIANT_INSIGHT":
            default:
                badgeTitle = "Brilliant Insight";
                category = "ACADEMIC";
                emoji = "💡";
                points = 15;
                break;
        }

        ClassroomBadgeAward award = ClassroomBadgeAward.builder()
                .tenantId(tenantId)
                .studentId(request.getStudentId())
                .studentName(request.getStudentName())
                .className(request.getClassName())
                .sectionName(request.getSectionName())
                .awardedByStaffName(teacherName)
                .badgeCode(request.getBadgeCode())
                .badgeTitle(emoji + " " + badgeTitle)
                .badgeCategory(category)
                .iconEmoji(emoji)
                .houseName(request.getHouseName())
                .housePointsAwarded(points)
                .teacherNotes(request.getTeacherNotes())
                .awardedAt(LocalDateTime.now())
                .build();

        ClassroomBadgeAward saved = badgeRepository.save(award);

        // Credit to School House if houseName provided
        if (request.getHouseName() != null && !request.getHouseName().isBlank()) {
            houseRepository.findByTenantIdAndHouseName(tenantId, request.getHouseName()).ifPresent(h -> {
                h.setTotalPoints(h.getTotalPoints() + points);
                houseRepository.save(h);
                log.info("Credited +{} house points to {} for student {}", points, h.getHouseName(), request.getStudentName());
            });
        }

        log.info("Teacher {} awarded badge '{}' to student {}", teacherName, badgeTitle, request.getStudentName());
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public StudentBadgeSummaryDTO getStudentBadgeSummary(UUID studentId) {
        List<ClassroomBadgeAward> badges = badgeRepository.findByStudentIdOrderByAwardedAtDesc(studentId);
        int totalPoints = badges.stream().mapToInt(ClassroomBadgeAward::getHousePointsAwarded).sum();

        String name = badges.isEmpty() ? "Student" : badges.get(0).getStudentName();
        String className = badges.isEmpty() ? "Class 10" : badges.get(0).getClassName();
        String houseName = badges.isEmpty() ? "Mars (Red)" : badges.get(0).getHouseName();

        List<ClassroomBadgeDTO> dtos = badges.stream().map(this::toDto).collect(Collectors.toList());

        return StudentBadgeSummaryDTO.builder()
                .studentId(studentId)
                .studentName(name)
                .className(className)
                .houseName(houseName)
                .totalBadgesCount(badges.size())
                .totalHousePointsContributed(totalPoints)
                .recentBadges(dtos)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ClassroomBadgeDTO> getClassBadges(String className, String sectionName) {
        String tenantId = Optional.ofNullable(TenantContext.getTenantId()).orElse("tenant_apex_mumbai_01");
        return badgeRepository.findByTenantIdAndClassNameAndSectionNameOrderByAwardedAtDesc(tenantId, className, sectionName)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ClassroomBadgeDTO toDto(ClassroomBadgeAward b) {
        return ClassroomBadgeDTO.builder()
                .id(b.getId())
                .studentId(b.getStudentId())
                .studentName(b.getStudentName())
                .className(b.getClassName())
                .sectionName(b.getSectionName())
                .awardedByStaffName(b.getAwardedByStaffName())
                .badgeCode(b.getBadgeCode())
                .badgeTitle(b.getBadgeTitle())
                .badgeCategory(b.getBadgeCategory())
                .iconEmoji(b.getIconEmoji())
                .houseName(b.getHouseName())
                .housePointsAwarded(b.getHousePointsAwarded())
                .teacherNotes(b.getTeacherNotes())
                .awardedAt(b.getAwardedAt())
                .build();
    }
}

