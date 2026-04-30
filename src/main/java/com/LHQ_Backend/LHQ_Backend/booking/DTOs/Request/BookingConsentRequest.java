package com.LHQ_Backend.LHQ_Backend.booking.DTOs.Request;

import jakarta.validation.constraints.NotNull;

// PATCH /bookings/{id}/consent
public record BookingConsentRequest(@NotNull boolean consent) {
}
