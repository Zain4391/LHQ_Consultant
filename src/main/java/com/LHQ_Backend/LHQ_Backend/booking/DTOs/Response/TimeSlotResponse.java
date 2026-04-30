package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response;

import java.time.Instant;
import com.LHQ_Backend.LHQ_Backend.booking.enums.TimeSlotStatus;
import lombok.Builder;

@Builder
public record TimeSlotResponse(String id, String lawyerId,

        /*
         * null if this is a custom slot. Frontend can use this to distinguish template-generated vs
         * custom.
         */
        String templateId,

        Instant startTime, Instant endTime, TimeSlotStatus status,

        /*
         * Convenience flag — avoids frontend having to null-check templateId. Mapper sets this to:
         * templateId == null
         */
        boolean isCustom,

        Instant createdAt) {
}
