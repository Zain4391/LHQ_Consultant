package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Request;

import java.time.LocalTime;
import com.LHQ_Backend.LHQ_Backend.booking.enums.DayOfWeek;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AvailabilityTemplateRequest(

                @NotNull(message = "Day of week is required") DayOfWeek dayOfWeek,

                @NotNull(message = "Start time is required") LocalTime startTime,

                @NotNull(message = "End time is required") LocalTime endTime,

                @NotNull(message = "Slot duration is required") @Min(value = 15,
                                message = "Slot duration must be at least 15 minutes") @Max(
                                                value = 480,
                                                message = "Slot duration cannot exceed 8 hours") Integer slotDurationMinutes,

                Boolean isActive // defaults to true in entity if null
) {
}
