package com.schoolerp.modules.academics.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitMilestoneRequest {
    private UUID projectId;
    private String groupName;
    private String groupLeaderName;
    private String memberNames; // e.g., "Aarav Sharma, Rhea Patel, Neil Sen"
    private int milestoneNumber; // 1 to 4
    private String milestoneTitle;
    private String submissionUrl;
}

