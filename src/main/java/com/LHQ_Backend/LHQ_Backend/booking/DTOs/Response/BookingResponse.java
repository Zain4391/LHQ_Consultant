package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response;

import com.LHQ_Backend.LHQ_Backend.booking.enums.BookingStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record BookingResponse(String id, String userId, String userFullName, // denormalized for
                                                                             // frontend convenience
        String lawyerId, String lawyerFullName, // denormalized for frontend convenience
        TimeSlotResponse timeSlot, // embedded — client needs slot details
        BookingStatus status, BigDecimal charges, String notes, Instant createdAt) {
}
