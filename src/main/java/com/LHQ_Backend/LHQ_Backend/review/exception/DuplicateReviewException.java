package com.LHQ_Backend.LHQ_Backend.review.exception;

import com.LHQ_Backend.LHQ_Backend.config.exception.ConflictException;

public class DuplicateReviewException extends ConflictException {
    public DuplicateReviewException(String bookingId) {
        super("A review already exists for booking: " + bookingId);
    }
}
