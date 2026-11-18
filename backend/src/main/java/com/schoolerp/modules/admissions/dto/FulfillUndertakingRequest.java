package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FulfillUndertakingRequest {
    private LocalDate fulfillmentDate;
    private String remarks;
}
