package com.LHQ_Backend.LHQ_Backend.user.DTOs.Request;

import com.LHQ_Backend.LHQ_Backend.user.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserRequest(

                @NotBlank(message = "First name is required") @Size(max = 50,
                                message = "First name must not exceed 50 characters") String firstName,

                @NotBlank(message = "Last name is required") @Size(max = 50,
                                message = "Last name must not exceed 50 characters") String lastName,

                @NotBlank(message = "Email is required") @Email(
                                message = "Email must be valid") String email,

                @NotBlank(message = "Password is required") @Size(min = 8,
                                message = "Password must be at least 8 characters") String password,

                @Min(value = 18, message = "Age must be at least 18") @Max(value = 120,
                                message = "Age must be realistic") Integer age,

                @NotNull(message = "Role is required") Role role) {
}
