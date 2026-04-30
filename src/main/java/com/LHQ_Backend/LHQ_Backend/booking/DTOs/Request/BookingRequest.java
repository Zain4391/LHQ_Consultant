package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record BookingRequest(

        @NotBlank(message = "Lawyer ID is required") String lawyerId,

        @NotBlank(message = "Time slot ID is required") String timeSlotId,

        @Size(max = 2000, message = "Notes must not exceed 2000 characters") String notes

// userId is resolved from the authenticated principal — never trust client-supplied userId
// charges are derived from lawyer's rate — not accepted from client
) {
}
