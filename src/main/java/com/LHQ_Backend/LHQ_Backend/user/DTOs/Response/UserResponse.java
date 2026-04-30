package com.LHQ_Backend.LHQ_Backend.user.DTOs.Response;

import java.time.Instant;

import com.LHQ_Backend.LHQ_Backend.user.enums.Role;

import lombok.Builder;

@Builder
public record UserResponse(String id, String firstName, String lastName, String email, Integer age,
        Role role, String profileImageUrl, Instant createdAt) {
}
