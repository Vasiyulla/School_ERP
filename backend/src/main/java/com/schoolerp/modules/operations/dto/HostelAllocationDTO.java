package com.schoolerp.modules.operations.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostelAllocationDTO {
    private UUID id;
    private UUID roomId;
    private String blockName;
    private String roomNumber;
    private UUID studentId;
    private String studentName;
    private String studentAdmissionNumber;
    private int bedNumber;
    private String academicYear;
    private String status;
    private LocalDateTime allocatedAt;
}

