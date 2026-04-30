package com.LHQ_Backend.LHQ_Backend.user.DTOs.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Used for PATCH /users/{id} All fields are optional — only non-null fields are applied. Role
 * changes must go through a dedicated admin endpoint, not here.
 */
@Builder
public record UserUpdateRequest(

        @Size(max = 50, message = "First name must not exceed 50 characters") String firstName,

        @Size(max = 50, message = "Last name must not exceed 50 characters") String lastName,

        @Min(value = 18, message = "Age must be at least 18") @Max(value = 120,
                message = "Age must be realistic") Integer age,

        @Size(max = 500, message = "Profile image URL too long") String profileImageUrl) {
}
