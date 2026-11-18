package com.schoolerp.modules.admissions.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveQueueBoardDTO {
    private long totalWaiting;
    private long totalServing;
    private long totalCompletedToday;
    private List<AdmissionQueueTokenDTO> servingTokens;
    private List<AdmissionQueueTokenDTO> nextInLineTokens;
}
