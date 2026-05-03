package com.LHQ_Backend.LHQ_Backend.lawyer.exception;

import com.LHQ_Backend.LHQ_Backend.config.exception.ConflictException;

public class LawyerProfileAlreadyExistsException extends ConflictException {
    public LawyerProfileAlreadyExistsException(String userId) {
        super("A lawyer profile already exists for user: " + userId);
    }
}
