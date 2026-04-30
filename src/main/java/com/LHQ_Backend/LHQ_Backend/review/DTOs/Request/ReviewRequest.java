package com.LHQ_Backend.LHQ_Backend.review.DTOs.Request;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record ReviewRequest(

        @NotBlank(message = "Booking ID is required") String bookingId,

        // lawyerId is derived from the booking — not accepted from client to prevent spoofing

        @NotNull(message = "Rating is required") @Min(value = 1,
                message = "Rating must be at least 1") @Max(value = 5,
                        message = "Rating must not exceed 5") Integer rating,

        @Size(max = 2000, message = "Comment must not exceed 2000 characters") String comment

// sentiment is computed server-side (NLP/rule-based) — not accepted from client
) {
}
