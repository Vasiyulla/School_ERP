package com.schoolerp.modules.students.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustodyUpdateRequest {
    private String custodyStatus; // ORIGINAL_IN_CUSTODY, VERIFIED_COPY, PENDING, RETURNED
    private String lockerIndex;   // e.g. Locker B-12 / File #2026-0842
    private String verifierName;
}

