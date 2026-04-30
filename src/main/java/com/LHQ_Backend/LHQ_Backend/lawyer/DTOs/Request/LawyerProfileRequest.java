package com.LHQ_Backend.LHQ_Backend.lawyer.DTOs.Request;

import java.math.BigDecimal;
import java.util.Set;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record LawyerProfileRequest(
        @Size(max = 1000, message = "Bio must not exceed 1000 characters") String bio,
        @Size(max = 5000, message = "About section must not exceed 5000 characters") String about,

        @NotNull(message = "Rate is required") @DecimalMin(value = "1.0", inclusive = true) @Digits(
                integer = 0, fraction = 2, message = "Rate format is invalid") BigDecimal rate,

        @NotBlank(message = "Bar number is required") String barNumber,

        @NotNull(message = "Years of experience is required") @Min(value = 0,
                message = "Years of experience cannot be negative") @Max(value = 60,
                        message = "Years of experience seems unrealistic") Integer yearsOfExperience,

        Set<String> specialtyIds) {
}
