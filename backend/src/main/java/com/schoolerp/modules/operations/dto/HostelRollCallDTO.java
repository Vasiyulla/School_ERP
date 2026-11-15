package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelRollCallDTO {
    private UUID id;
    private UUID allocationId;
    private UUID studentId;
    private String studentName;
    private LocalDate rollCallDate;
    private String sessionType;
    private String status;
    private String remarks;
    private String wardenName;
    private LocalDateTime createdAt;
}

