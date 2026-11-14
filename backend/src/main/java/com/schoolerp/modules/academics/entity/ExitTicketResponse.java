package com.schoolerp.modules.academics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exit_ticket_responses", schema = "academics")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExitTicketResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID sessionId;

    @Column(nullable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String studentName;

    private String selectedAnswer;

    private Boolean isCorrect;

    @Column(nullable = false)
    private int confidenceScore; // 1 = Struggling, 2 = Unsure, 3 = Understood

    @Column(columnDefinition = "TEXT")
    private String studentNotes;

    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();
}

