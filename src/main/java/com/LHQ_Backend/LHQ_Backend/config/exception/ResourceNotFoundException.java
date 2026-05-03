package com.LHQ_Backend.LHQ_Backend.config.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // → "User not found with id: abc-123"
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(resource + " not found with " + field + ": " + value);
    }
}
