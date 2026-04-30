package com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request;

import com.LHQ_Backend.LHQ_Backend.user.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
    public record RegisterRequest(
 
            @NotBlank(message = "First name is required")
            @Size(max = 50)
            String firstName,
 
            @NotBlank(message = "Last name is required")
            @Size(max = 50)
            String lastName,
 
            @NotBlank(message = "Email is required")
            @Email(message = "Email must be valid")
            String email,
 
            @NotBlank(message = "Password is required")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password,
 
            @Min(18) @Max(120)
            Integer age,
 
            @NotNull(message = "Role is required")
            Role role
    ) {}
