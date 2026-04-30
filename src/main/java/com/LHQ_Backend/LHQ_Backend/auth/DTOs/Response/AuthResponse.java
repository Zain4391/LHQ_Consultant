package com.LHQ_Backend.LHQ_Backend.auth.DTOs.Response;

import com.LHQ_Backend.LHQ_Backend.user.enums.Role;
import lombok.Builder;

/**
* Returned on successful login or register.
* accessToken: short-lived JWT (e.g. 15 min)
* refreshToken: long-lived (e.g. 7 days), stored HttpOnly cookie ideally
*/

@Builder
    public record AuthResponse(
            String accessToken,
            String refreshToken,
            String tokenType,       // always "Bearer"
            Long expiresIn,         // seconds until accessToken expires
            String userId,
            Role role
    ) {}
