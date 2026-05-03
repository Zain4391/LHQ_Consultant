package com.LHQ_Backend.LHQ_Backend.auth.exception;

import com.LHQ_Backend.LHQ_Backend.config.exception.BusinessRuleViolationException;

public class TokenRefreshException extends BusinessRuleViolationException {
    public TokenRefreshException(String reason) {
        super("Token refresh failed: " + reason);
    }
}
