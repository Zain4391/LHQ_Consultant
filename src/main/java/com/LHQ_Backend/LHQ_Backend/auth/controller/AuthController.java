package com.LHQ_Backend.LHQ_Backend.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.LoginRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RefreshTokenRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RegisterRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Response.AuthResponse;
import com.LHQ_Backend.LHQ_Backend.auth.service.AuthServiceImpl;
import com.LHQ_Backend.LHQ_Backend.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    /**
     * Logout requires an authenticated request (valid access token). The refresh token to
     * invalidate is passed in the body. The access token itself expires naturally (add deny-list if
     * needed).
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request,
            @AuthenticationPrincipal User principal) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
