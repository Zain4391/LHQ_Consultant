package com.LHQ_Backend.LHQ_Backend.cases.DTOs.Request;

import com.LHQ_Backend.LHQ_Backend.cases.enums.ClientLawyerStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ClientLawyerStatusUpdateRequest(

        @NotNull(message = "Status is required") ClientLawyerStatus status) {
}
