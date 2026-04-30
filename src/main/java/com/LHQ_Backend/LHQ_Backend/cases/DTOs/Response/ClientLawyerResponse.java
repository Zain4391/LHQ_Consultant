package com.LHQ_Backend.LHQ_Backend.cases.DTOs.Response;

import com.LHQ_Backend.LHQ_Backend.cases.enums.ClientLawyerStatus;
import lombok.Builder;

import java.time.Instant;

/**
 * ClientLawyer relationships are created automatically when a booking is CONFIRMED. They are not
 * created manually via API — hence no Request DTO.
 *
 * Status updates (CLOSED, SUSPENDED) handled via PATCH /client-lawyer/{id}/status
 */
@Builder
public record ClientLawyerResponse(String id, String userId, String userFullName, String lawyerId,
        String lawyerFullName, ClientLawyerStatus status, Instant createdAt) {
}
