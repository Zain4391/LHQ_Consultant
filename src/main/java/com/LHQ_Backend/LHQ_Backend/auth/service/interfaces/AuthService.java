package com.LHQ_Backend.LHQ_Backend.auth.service.interfaces;

import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.LoginRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RefreshTokenRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RegisterRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(String refreshToken);
}
