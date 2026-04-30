package com.LHQ_Backend.LHQ_Backend.cases.DTOs.Request;

import com.LHQ_Backend.LHQ_Backend.cases.enums.CaseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * Used by PATCH /cases/{id}/status Only the assigned lawyer or ADMIN can update case status.
 */
@Builder
public record CaseStatusUpdateRequest(

        @NotNull(message = "Status is required") CaseStatus status) {
}
