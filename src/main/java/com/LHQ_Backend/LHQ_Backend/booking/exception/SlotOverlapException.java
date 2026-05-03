package com.LHQ_Backend.LHQ_Backend.booking.exception;

import com.LHQ_Backend.LHQ_Backend.config.exception.ConflictException;

public class SlotOverlapException extends ConflictException {
    public SlotOverlapException() {
        super("A time slot already exists within the requested window for this lawyer.");
    }
}
