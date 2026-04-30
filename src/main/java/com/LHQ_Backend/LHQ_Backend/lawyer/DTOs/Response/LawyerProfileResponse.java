package com.LHQ_Backend.LHQ_Backend.lawyer.DTOs.Response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import lombok.Builder;

@Builder
public record LawyerProfileResponse(String id, String userId, String firstName, // denormalized from
                                                                                // User for
                                                                                // convenience
        String lastName, String email, String bio, String about, BigDecimal rate, String barNumber,
        Integer yearsOfExperience, Set<SpecialtyResponse> specialties, Instant createdAt) {
}
