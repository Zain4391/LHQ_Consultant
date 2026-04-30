package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Request;

import com.LHQ_Backend.LHQ_Backend.booking.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/*
 * Used by PATCH /bookings/{id}/status Lawyers: CONFIRMED, CANCELLED, COMPLETED Users: CANCELLED
 * (within cancellation window) After COMPLETED, event pushed to queue, send notification to the
 * user and show the updated booking status in their dashboard and store it in DB.
 */
@Builder
public record BookingStatusUpdateRequest(

                @NotNull(message = "Status is required") BookingStatus status) {
}
