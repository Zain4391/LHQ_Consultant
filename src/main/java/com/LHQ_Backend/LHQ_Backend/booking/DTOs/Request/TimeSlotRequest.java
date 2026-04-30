package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;

/**
 * Used by POST /time-slots/custom Only callable by LAWYER role (enforced at controller level).
 *
 * lawyerId is resolved from the authenticated principal — never from request body. template_id will
 * be NULL in the persisted entity — this is a custom slot.
 *
 * Service layer MUST validate: 1. startTime is before endTime 2. Minimum slot duration (e.g. 15
 * minutes) 3. No overlap with existing slots for this lawyer on this day
 */
@Builder
public record TimeSlotRequest(

        @NotNull(message = "Start time is required") @Future(
                message = "Start time must be in the future") Instant startTime,

        @NotNull(message = "End time is required") @Future(
                message = "End time must be in the future") Instant endTime) {
}
