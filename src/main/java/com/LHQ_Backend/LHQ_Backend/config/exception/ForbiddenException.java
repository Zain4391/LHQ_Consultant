package com.LHQ_Backend.LHQ_Backend.config.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
