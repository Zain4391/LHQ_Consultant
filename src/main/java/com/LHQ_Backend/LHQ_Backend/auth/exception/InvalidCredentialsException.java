package com.LHQ_Backend.LHQ_Backend.auth.exception;

import com.LHQ_Backend.LHQ_Backend.config.exception.BusinessRuleViolationException;

public class InvalidCredentialsException extends BusinessRuleViolationException {
    public InvalidCredentialsException() {
        super("Invalid email or password.");
    }
}
