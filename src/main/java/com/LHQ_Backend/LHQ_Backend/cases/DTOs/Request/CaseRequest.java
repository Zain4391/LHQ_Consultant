package com.LHQ_Backend.LHQ_Backend.cases.DTOs.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CaseRequest(

        @NotNull(message = "Client-lawyer relationship ID is required") String clientLawyerId,

        @NotBlank(message = "Case title is required") @Size(max = 255,
                message = "Title must not exceed 255 characters") String title,

        @Size(max = 5000,
                message = "Description must not exceed 5000 characters") String description,

        @Size(max = 100, message = "Case type must not exceed 100 characters") String caseType

// status defaults to OPEN on creation
// openedAt is set server-side
) {
}
