package com.LHQ_Backend.LHQ_Backend.config.DTOs.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(int status, String error, String message, String path,
        Instant timestamp, Map<String, String> fieldErrors // only present on validation failures
) {

    // no field errors
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path, Instant.now(), null);
    }

    // For validation failures
    public static ErrorResponse ofValidation(int status, String error, String message, String path,
            Map<String, String> fieldErrors) {
        return new ErrorResponse(status, error, message, path, Instant.now(), fieldErrors);
    }
}
