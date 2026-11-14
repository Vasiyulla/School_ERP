package com.schoolerp.modules.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiometricPunchPayloadItemDTO {
    private String staffCode;
    private String staffName;
    private LocalDateTime punchTime;
    private String punchType; // IN, OUT
}

