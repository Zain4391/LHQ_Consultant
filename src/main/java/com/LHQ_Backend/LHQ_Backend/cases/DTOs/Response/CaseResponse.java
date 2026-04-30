package com.LHQ_Backend.LHQ_Backend.cases.DTOs.Response;

import com.LHQ_Backend.LHQ_Backend.cases.enums.CaseStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CaseResponse(String id, String clientLawyerId, String userId, // denormalized from
                                                                            // ClientLawyer
        String userFullName, String lawyerId, // denormalized from ClientLawyer
        String lawyerFullName, String title, String description, String caseType, CaseStatus status,
        Instant openedAt, Instant closedAt) {
}
