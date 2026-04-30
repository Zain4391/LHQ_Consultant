package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response;

import java.time.Instant;
import java.time.LocalTime;
import com.LHQ_Backend.LHQ_Backend.booking.enums.DayOfWeek;
import lombok.Builder;

@Builder
public record AvailabilityTemplateResponse(String id, String lawyerId, DayOfWeek dayOfWeek,
                LocalTime startTime, LocalTime endTime, Integer slotDurationMinutes,
                Boolean isActive, Instant createdAt) {
}
