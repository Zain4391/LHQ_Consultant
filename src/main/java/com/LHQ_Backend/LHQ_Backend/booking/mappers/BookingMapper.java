package com.LHQ_Backend.LHQ_Backend.booking.mappers;

import com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response.AvailabilityTemplateResponse;
import com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response.BookingResponse;
import com.LHQ_Backend.LHQ_Backend.booking.DTOs.Response.TimeSlotResponse;
import com.LHQ_Backend.LHQ_Backend.booking.entity.AvailabilityTemplate;
import com.LHQ_Backend.LHQ_Backend.booking.entity.Booking;
import com.LHQ_Backend.LHQ_Backend.booking.entity.TimeSlot;

public final class BookingMapper {

    private BookingMapper() {}

    public static BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder().id(booking.getId()).userId(booking.getUser().getId())
                .userFullName(
                        booking.getUser().getFirstName() + " " + booking.getUser().getLastName())
                .lawyerId(booking.getLawyer().getId())
                .lawyerFullName(booking.getLawyer().getUser().getFirstName() + " "
                        + booking.getLawyer().getUser().getLastName())
                .timeSlot(toTimeSlotResponse(booking.getTimeSlot())).status(booking.getStatus())
                .charges(booking.getCharges()).notes(booking.getNotes())
                .createdAt(booking.getCreatedAt()).build();
    }

    public static TimeSlotResponse toTimeSlotResponse(TimeSlot slot) {
        return TimeSlotResponse.builder().id(slot.getId()).lawyerId(slot.getLawyer().getId())
                .templateId(slot.getTemplate() != null ? slot.getTemplate().getId() : null)
                .startTime(slot.getStartTime()).endTime(slot.getEndTime()).status(slot.getStatus())
                .isCustom(slot.getTemplate() == null) // convenience flag
                .createdAt(slot.getCreatedAt()).build();
    }

    public static AvailabilityTemplateResponse toTemplateResponse(AvailabilityTemplate template) {
        return AvailabilityTemplateResponse.builder().id(template.getId())
                .lawyerId(template.getLawyer().getId()).dayOfWeek(template.getDayOfWeek())
                .startTime(template.getStartTime()).endTime(template.getEndTime())
                .slotDurationMinutes(template.getSlotDurationMinutes())
                .isActive(template.getIsActive()).createdAt(template.getCreatedAt()).build();
    }
}
