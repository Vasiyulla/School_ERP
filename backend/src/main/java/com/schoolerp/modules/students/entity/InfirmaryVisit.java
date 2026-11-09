package com.schoolerp.modules.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "infirmary_visits", schema = "students")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfirmaryVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private LocalDateTime visitTimestamp;

    @Column(nullable = false)
    private String chiefComplaint; // FEVER, HEADACHE, INJURY_FALL, STOMACH_ACHE, ASTHMA_DYSPNEA, ALLERGIC_REACTION

    private Double temperatureFahrenheit; // e.g. 101.4

    private String bloodPressure; // e.g. 110/70

    private Integer pulseBpm; // e.g. 78

    private String firstAidGiven; // "Paracetamol 250mg oral, Cold sponge compress, Betadine dressing"

    @Builder.Default
    private int restDurationMinutes = 30;

    @Column(nullable = false)
    @Builder.Default
    private String disposition = "RESTING"; // RETURNED_TO_CLASS, SENT_HOME_WITH_PARENT, REFERRED_TO_HOSPITAL

    @Column(nullable = false)
    private String attendingNurse; // "Sister Mary D'Souza, RN"

    @Builder.Default
    private boolean parentNotified = false;

    private String parentAlertChannel; // WHATSAPP, SMS, PHONE_CALL
}

