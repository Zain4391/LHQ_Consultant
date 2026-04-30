package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Request;

import com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TimeSlotStatusUpdateRequest(

        @NotNull(message = "Status is required") TimeSlotStatus status) {
}
