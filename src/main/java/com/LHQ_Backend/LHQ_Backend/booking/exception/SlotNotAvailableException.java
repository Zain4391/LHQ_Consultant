package com.LHQ_Backend.LHQ_Backend.booking.exception;

import com.LHQ_Backend.LHQ_Backend.config.exception.BusinessRuleViolationException;

public class SlotNotAvailableException extends BusinessRuleViolationException {
    public SlotNotAvailableException(String slotId) {
        super("Time slot is not available for booking: " + slotId);
    }
}
