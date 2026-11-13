package com.schoolerp.modules.academics.service;

import com.schoolerp.common.context.TenantContext;
import com.schoolerp.modules.academics.dto.CreateSchoolEventRequest;
import com.schoolerp.modules.academics.dto.EnrollParticipantRequest;
import com.schoolerp.modules.academics.dto.RecordParticipantResultRequest;
import com.schoolerp.modules.academics.entity.EventParticipant;
import com.schoolerp.modules.academics.entity.SchoolEvent;
import com.schoolerp.modules.academics.repository.EventParticipantRepository;
import com.schoolerp.modules.academics.repository.SchoolEventRepository;
import com.schoolerp.modules.students.entity.Student;
import com.schoolerp.modules.students.repository.StudentRepository;
import com.schoolerp.modules.students.service.BehaviorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolEventService {

    private final SchoolEventRepository eventRepository;
    private final EventParticipantRepository participantRepository;
    private final StudentRepository studentRepository;
    private final BehaviorService behaviorService;

    private String resolveTenant() {
        String tenantId = TenantContext.getTenantId();
        return (tenantId != null && !tenantId.isBlank()) ? tenantId : "tenant-default";
    }

    @Transactional
    public SchoolEvent createEvent(CreateSchoolEventRequest request, String teacherName) {
        String tenantId = resolveTenant();

        SchoolEvent event = SchoolEvent.builder()
                .tenantId(tenantId)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory().toUpperCase())
                .eventDate(request.getEventDate())
                .endDate(request.getEndDate())
                .venue(request.getVenue())
                .coordinatorTeacherName(request.getCoordinatorTeacherName() != null ? request.getCoordinatorTeacherName() : teacherName)
                .targetClasses(request.getTargetClasses() != null ? request.getTargetClasses() : "ALL")
                .maxParticipants(request.getMaxParticipants() != null ? request.getMaxParticipants() : 100)
                .budgetAllocated(request.getBudgetAllocated())
                .status("PUBLISHED")
                .build();

        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<SchoolEvent> getEvents(String category, String status, Boolean upcomingOnly) {
        String tenantId = resolveTenant();

        if (upcomingOnly != null && upcomingOnly) {
            return eventRepository.findAllByTenantIdAndEventDateGreaterThanEqualOrderByEventDateAsc(tenantId, LocalDate.now());
        }
        if (category != null && !category.isBlank() && !"ALL".equalsIgnoreCase(category)) {
            return eventRepository.findAllByTenantIdAndCategoryOrderByEventDateDesc(tenantId, category.toUpperCase());
        }
        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return eventRepository.findAllByTenantIdAndStatusOrderByEventDateDesc(tenantId, status.toUpperCase());
        }
        return eventRepository.findAllByTenantIdOrderByEventDateDesc(tenantId);
    }

    @Transactional(readOnly = true)
    public SchoolEvent getEventById(UUID eventId) {
        String tenantId = resolveTenant();
        return eventRepository.findByIdAndTenantId(eventId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("School Event not found with ID: " + eventId));
    }

    @Transactional
    public SchoolEvent updateEventStatus(UUID eventId, String newStatus) {
        SchoolEvent event = getEventById(eventId);
        event.setStatus(newStatus.toUpperCase());
        return eventRepository.save(event);
    }

    @Transactional
    public List<EventParticipant> enrollParticipants(UUID eventId, EnrollParticipantRequest request) {
        String tenantId = resolveTenant();
        SchoolEvent event = getEventById(eventId);

        List<EventParticipant> enrolledList = new ArrayList<>();

        for (EnrollParticipantRequest.ParticipantItem item : request.getParticipants()) {
            Optional<EventParticipant> existing = participantRepository.findByTenantIdAndEventIdAndStudentId(tenantId, eventId, item.getStudentId());
            if (existing.isPresent()) {
                EventParticipant p = existing.get();
                p.setRole(item.getRole() != null ? item.getRole() : "PARTICIPANT");
                enrolledList.add(participantRepository.save(p));
                continue;
            }

            Student student = studentRepository.findByIdAndTenantId(item.getStudentId(), tenantId)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + item.getStudentId()));

            EventParticipant participant = EventParticipant.builder()
                    .tenantId(tenantId)
                    .eventId(event.getId())
                    .studentId(student.getId())
                    .studentAdmissionNo(student.getAdmissionNumber())
                    .studentName(student.getFullName())
                    .className(student.getClassName())
                    .sectionName(student.getSectionName())
                    .role(item.getRole() != null ? item.getRole() : "PARTICIPANT")
                    .participationStatus("CONFIRMED")
                    .achievementTier("NONE")
                    .housePointsAwarded(0)
                    .certificateIssued(false)
                    .build();

            enrolledList.add(participantRepository.save(participant));
        }

        return enrolledList;
    }

    @Transactional
    public List<EventParticipant> enrollClassSection(UUID eventId, String className, String sectionName, String role) {
        String tenantId = resolveTenant();
        SchoolEvent event = getEventById(eventId);

        List<Student> students;
        if (sectionName != null && !sectionName.isBlank() && !"ALL".equalsIgnoreCase(sectionName)) {
            students = studentRepository.findAllByTenantIdAndClassNameAndSectionNameOrderByRollNumberAsc(tenantId, className, sectionName);
        } else {
            students = studentRepository.findAllByTenantIdAndClassNameOrderByRollNumberAsc(tenantId, className);
        }

        List<EventParticipant> result = new ArrayList<>();
        String assignedRole = (role != null && !role.isBlank()) ? role : "PARTICIPANT";

        for (Student s : students) {
            Optional<EventParticipant> existing = participantRepository.findByTenantIdAndEventIdAndStudentId(tenantId, eventId, s.getId());
            if (existing.isEmpty()) {
                EventParticipant p = EventParticipant.builder()
                        .tenantId(tenantId)
                        .eventId(event.getId())
                        .studentId(s.getId())
                        .studentAdmissionNo(s.getAdmissionNumber())
                        .studentName(s.getFullName())
                        .className(s.getClassName())
                        .sectionName(s.getSectionName())
                        .role(assignedRole)
                        .participationStatus("CONFIRMED")
                        .achievementTier("NONE")
                        .housePointsAwarded(0)
                        .certificateIssued(false)
                        .build();
                result.add(participantRepository.save(p));
            } else {
                result.add(existing.get());
            }
        }

        return result;
    }

    @Transactional(readOnly = true)
    public List<EventParticipant> getEventParticipants(UUID eventId) {
        String tenantId = resolveTenant();
        return participantRepository.findAllByTenantIdAndEventIdOrderByStudentNameAsc(tenantId, eventId);
    }

    @Transactional
    public EventParticipant recordParticipantResult(UUID eventId, UUID participantId,
                                                   RecordParticipantResultRequest request,
                                                   String evaluatingTeacher) {
        String tenantId = resolveTenant();
        SchoolEvent event = getEventById(eventId);

        EventParticipant participant = participantRepository.findByIdAndTenantId(participantId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Event participant not found with ID: " + participantId));

        if (!participant.getEventId().equals(eventId)) {
            throw new IllegalArgumentException("Participant does not belong to specified event: " + eventId);
        }

        if (request.getParticipationStatus() != null) {
            participant.setParticipationStatus(request.getParticipationStatus().toUpperCase());
        }
        if (request.getAchievementTier() != null) {
            participant.setAchievementTier(request.getAchievementTier().toUpperCase());
        }
        if (request.getTeacherRemarks() != null) {
            participant.setTeacherRemarks(request.getTeacherRemarks());
        }

        // Handle certificate generation if requested
        if (request.isIssueCertificate() && !participant.isCertificateIssued()) {
            participant.setCertificateIssued(true);
            String certCode = String.format("CERT-EVT-%d-%s",
                    LocalDate.now().getYear(),
                    UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            participant.setCertificateNumber(certCode);
        }

        // Handle House Trophy points bridge
        int points = request.getHousePointsAwarded();
        if (points > 0) {
            participant.setHousePointsAwarded(points);
            try {
                String desc = String.format("Co-Curricular Achievement: %s in %s (%s)",
                        participant.getAchievementTier(), event.getTitle(), event.getCategory());
                behaviorService.recordBehaviorIncident(
                        participant.getStudentId(),
                        "MERIT",
                        "CO_CURRICULAR_EVENT",
                        points,
                        desc,
                        evaluatingTeacher != null ? evaluatingTeacher : event.getCoordinatorTeacherName()
                );
                log.info("Successfully awarded {} House points to student {} for event {}",
                        points, participant.getStudentName(), event.getTitle());
            } catch (Exception ex) {
                log.warn("House points recording bridge note: {}", ex.getMessage());
            }
        }

        return participantRepository.save(participant);
    }

    /**
     * Generate an event-day call sheet / printable roll sheet with check-in boxes.
     */
    public String generateEventCallSheetHtml(UUID eventId) {
        SchoolEvent event = getEventById(eventId);
        List<EventParticipant> participants = getEventParticipants(eventId);
        String eventDateStr = event.getEventDate().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n")
            .append("<title>Event Call Sheet - ").append(event.getTitle()).append("</title>\n")
            .append("<style>\n")
            .append("  body { font-family: 'Segoe UI', Arial, sans-serif; margin: 20px; color: #0f172a; }\n")
            .append("  .header { border-bottom: 2px solid #1e3a8a; padding-bottom: 12px; margin-bottom: 20px; text-align: center; }\n")
            .append("  .header h1 { margin: 0; font-size: 22px; color: #1e3a8a; text-transform: uppercase; }\n")
            .append("  .header p { margin: 4px 0 0; font-size: 13px; color: #475569; }\n")
            .append("  .event-banner { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 6px; padding: 12px 16px; margin-bottom: 20px; display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; font-size: 13px; }\n")
            .append("  .action-bar { margin-bottom: 20px; display: flex; justify-content: flex-end; }\n")
            .append("  .btn-print { background: #1e3a8a; color: #fff; border: none; padding: 8px 16px; border-radius: 6px; font-weight: 600; cursor: pointer; }\n")
            .append("  table { width: 100%; border-collapse: collapse; font-size: 12px; }\n")
            .append("  th, td { border: 1px solid #cbd5e1; padding: 8px 10px; text-align: left; }\n")
            .append("  th { background-color: #1e3a8a; color: #ffffff; text-align: center; font-weight: 600; }\n")
            .append("  .text-center { text-align: center; }\n")
            .append("  .check-box { width: 50px; text-align: center; }\n")
            .append("  .signatures { display: flex; justify-content: space-between; margin-top: 60px; font-size: 12px; }\n")
            .append("  .sig-line { border-top: 1px solid #475569; width: 200px; text-align: center; padding-top: 6px; }\n")
            .append("  @media print {\n")
            .append("    .action-bar { display: none !important; }\n")
            .append("    th { background-color: #e2e8f0 !important; color: #000 !important; -webkit-print-color-adjust: exact; }\n")
            .append("  }\n")
            .append("</style>\n</head>\n<body>\n")
            .append("<div class=\"action-bar\"><button class=\"btn-print\" onclick=\"window.print()\">Print Event Call Sheet</button></div>\n")
            .append("<div class=\"header\">\n")
            .append("  <h1>Official School Event Call Sheet & Participation Roster</h1>\n")
            .append("  <p>Co-Curricular & Student Talent Department | National Public Academy</p>\n")
            .append("</div>\n")
            .append("<div class=\"event-banner\">\n")
            .append("  <div><strong>Event:</strong> ").append(event.getTitle()).append("</div>\n")
            .append("  <div><strong>Category:</strong> ").append(event.getCategory()).append("</div>\n")
            .append("  <div><strong>Date:</strong> ").append(eventDateStr).append("</div>\n")
            .append("  <div><strong>Venue:</strong> ").append(event.getVenue()).append("</div>\n")
            .append("  <div><strong>Coordinator:</strong> ").append(event.getCoordinatorTeacherName() != null ? event.getCoordinatorTeacherName() : "Faculty In-Charge").append("</div>\n")
            .append("  <div><strong>Total Enrolled:</strong> ").append(participants.size()).append("</div>\n")
            .append("</div>\n")
            .append("<table>\n<thead>\n<tr>\n")
            .append("  <th>S.No</th>\n<th>Adm No</th>\n<th>Student Name</th>\n<th>Class & Section</th>\n")
            .append("  <th>Designated Role</th>\n<th>Attendance [ / ]</th>\n<th>Achievement Tier</th>\n<th>Judges / Teacher Notes</th>\n")
            .append("</tr>\n</thead>\n<tbody>\n");

        int serial = 1;
        for (EventParticipant p : participants) {
            html.append("<tr>\n")
                .append("  <td class=\"text-center\">").append(serial++).append("</td>\n")
                .append("  <td class=\"text-center\">").append(p.getStudentAdmissionNo() != null ? p.getStudentAdmissionNo() : "-").append("</td>\n")
                .append("  <td><strong>").append(p.getStudentName()).append("</strong></td>\n")
                .append("  <td class=\"text-center\">").append(p.getClassName() != null ? p.getClassName() : "-")
                .append(" (").append(p.getSectionName() != null ? p.getSectionName() : "-").append(")</td>\n")
                .append("  <td class=\"text-center\"><span style=\"font-weight:600;\">").append(p.getRole()).append("</span></td>\n")
                .append("  <td class=\"check-box\">[ &nbsp; ]</td>\n")
                .append("  <td class=\"text-center\">").append(p.getAchievementTier() != null ? p.getAchievementTier() : "-").append("</td>\n")
                .append("  <td>").append(p.getTeacherRemarks() != null ? p.getTeacherRemarks() : "").append("</td>\n")
                .append("</tr>\n");
        }

        html.append("</tbody>\n</table>\n")
            .append("<div class=\"signatures\">\n")
            .append("  <div class=\"sig-line\">Event Coordinator Teacher</div>\n")
            .append("  <div class=\"sig-line\">Chief Judge / Stage Manager</div>\n")
            .append("  <div class=\"sig-line\">Principal / Head of School</div>\n")
            .append("</div>\n")
            .append("</body>\n</html>");

        return html.toString();
    }
}

