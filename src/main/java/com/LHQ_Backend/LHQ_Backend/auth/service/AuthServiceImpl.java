package com.LHQ_Backend.LHQ_Backend.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.ForgotPasswordRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.LoginRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RefreshTokenRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RegisterRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Response.AuthResponse;
import com.LHQ_Backend.LHQ_Backend.auth.exception.InvalidCredentialsException;
import com.LHQ_Backend.LHQ_Backend.auth.exception.PasswordMismatchException;
import com.LHQ_Backend.LHQ_Backend.auth.exception.TokenRefreshException;
import com.LHQ_Backend.LHQ_Backend.auth.service.interfaces.AuthService;
import com.LHQ_Backend.LHQ_Backend.config.exception.ConflictException;
import com.LHQ_Backend.LHQ_Backend.config.exception.ResourceNotFoundException;
import com.LHQ_Backend.LHQ_Backend.config.security.JwtService;
import com.LHQ_Backend.LHQ_Backend.config.security.RefreshTokenService;
import com.LHQ_Backend.LHQ_Backend.user.entity.User;
import com.LHQ_Backend.LHQ_Backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    private AuthResponse buildAuthResponse(User user) {
        String accessToken =
                jwtService.generateAccessToken(user, user.getId(), user.getRole().name());
        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
                .tokenType("Bearer").expiresIn(jwtService.getAccessTokenExpirySeconds())
                .userId(user.getId()).role(user.getRole()).build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException(
                    "An account with this email already exists: " + request.email());
        }

        User user = User.builder().firstName(request.firstName()).lastName(request.lastName())
                .email(request.email()).password(passwordEncoder.encode(request.password()))
                .age(request.age()).role(request.role()).isActive(true).build();

        User saved = userRepository.save(user);
        log.info("New user registered: id={} role={}", saved.getId(), saved.getRole());

        return buildAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            // AuthenticationManager handles: bad credentials, disabled account
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (DisabledException e) {
            throw new InvalidCredentialsException(); // don't leak "account disabled" detail
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        log.info("User logged in: id={}", user.getId());
        return buildAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshTokenRequest req) {

        String oldToken = req.refreshToken();

        /*
         * Get user id from refresh token and check: 1. If The id is null: Exception: Session
         * expired 2. Id found, but used account is deleted: Exception: Acc del 3. User found in DB
         * but account disabled: Exception: Acc disabled, token deleted
         */
        String userId = refreshTokenService.getUserIdFromRefreshToken(oldToken);
        if (userId == null) {
            throw new TokenRefreshException("Token not found or expired. Please login again");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TokenRefreshException("User no longer exists!"));

        if (!user.isEnabled()) {
            refreshTokenService.delete(oldToken);
            throw new TokenRefreshException("Account disabled");
        }

        String newRefreshToken = refreshTokenService.rotateRefreshToken(oldToken, userId);
        String newAccessToken =
                jwtService.generateAccessToken(user, user.getId(), user.getRole().name());

        log.debug("Refresh token rotated for userId={}", userId);

        return AuthResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshToken)
                .tokenType("Bearer").expiresIn(jwtService.getAccessTokenExpirySeconds())
                .userId(user.getId()).role(user.getRole()).build();

    }

    public void logout(String refreshToken) {
        refreshTokenService.delete(refreshToken);
        log.debug("Refresh token invalidated on logout");
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new PasswordMismatchException();
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.email()));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        log.info("Password reset via forgot-password for userId={}", user.getId());
    }
}
