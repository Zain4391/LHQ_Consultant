package com.LHQ_Backend.LHQ_Backend.auth.exception;

import com.LHQ_Backend.LHQ_Backend.config.exception.BusinessRuleViolationException;

public class PasswordMismatchException extends BusinessRuleViolationException {
    public PasswordMismatchException() {
        super("New password and confirmation password do not match.");
    }
}
