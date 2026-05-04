package com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ForgotPasswordRequest(

        @NotBlank(message = "Email is required") @Email(
                message = "Email must be valid") String email,

        @NotBlank(message = "New password is required") @Size(min = 8,
                message = "Password must be at least 8 characters") String newPassword,

        @NotBlank(message = "Please confirm your new password") String confirmPassword) {
}
