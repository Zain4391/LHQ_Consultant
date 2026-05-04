package com.LHQ_Backend.LHQ_Backend.auth.service;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.LoginRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RefreshTokenRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Request.RegisterRequest;
import com.LHQ_Backend.LHQ_Backend.auth.DTOs.Response.AuthResponse;
import com.LHQ_Backend.LHQ_Backend.auth.exception.InvalidCredentialsException;
import com.LHQ_Backend.LHQ_Backend.auth.exception.TokenRefreshException;
import com.LHQ_Backend.LHQ_Backend.config.exception.ConflictException;
import com.LHQ_Backend.LHQ_Backend.config.security.JwtService;
import com.LHQ_Backend.LHQ_Backend.config.security.RefreshTokenService;
import com.LHQ_Backend.LHQ_Backend.user.entity.User;
import com.LHQ_Backend.LHQ_Backend.user.enums.Role;
import com.LHQ_Backend.LHQ_Backend.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    // mock dependencies

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthenticationManager authenticationManager;

    // Inject them into the service being tested

    @InjectMocks
    private AuthServiceImpl authService;

    private User activeUser;
    private User disabledUser;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        activeUser = User.builder().id("user-uuid-123").firstName("Jane").lastName("Doe")
                .email("jane@example.com").password("hashed-password").role(Role.USER)
                .isActive(true).build();

        disabledUser = User.builder().id("user-uuid-456").firstName("John").lastName("Disabled")
                .email("disabled@example.com").password("hashed-password").role(Role.USER)
                .isActive(false).build();

        registerRequest = RegisterRequest.builder().firstName("Jane").lastName("Doe")
                .email("jane@example.com").password("SecurePass1!").age(30).role(Role.USER).build();
    }

    @Nested // group related testcases or scenarios
    @DisplayName("register()")
    class Register {

        @Test
        @DisplayName("success → saves user, returns token pair")
        void register_success() {
            when(userRepository.existsByEmail("jane@example.com")).thenReturn(false);
            when(passwordEncoder.encode("SecurePass1!")).thenReturn("hashed");
            when(userRepository.save(any(User.class))).thenReturn(activeUser);
            when(jwtService.generateAccessToken(any(), eq("user-uuid-123"), eq("USER")))
                    .thenReturn("access-token");
            when(jwtService.getAccessTokenExpirySeconds()).thenReturn(900L);
            when(refreshTokenService.createRefreshToken("user-uuid-123"))
                    .thenReturn("refresh-token");

            AuthResponse response = authService.register(registerRequest);

            assertThat(response.accessToken()).isEqualTo("access-token");
            assertThat(response.refreshToken()).isEqualTo("refresh-token");
            assertThat(response.tokenType()).isEqualTo("Bearer");
            assertThat(response.expiresIn()).isEqualTo(900L);
            assertThat(response.userId()).isEqualTo("user-uuid-123");
            assertThat(response.role()).isEqualTo(Role.USER);

            verify(userRepository).save(argThat(u -> u.getEmail().equals("jane@example.com")
                    && u.getPassword().equals("hashed") && u.getRole() == Role.USER));
        }

        @Test
        @DisplayName("duplicate email → ConflictException, no save")
        void register_duplicateEmail_throwsConflict() {
            when(userRepository.existsByEmail("jane@example.com")).thenReturn(true);

            // check if valid exception is thrown
            assertThatThrownBy(() -> authService.register(registerRequest))
                    .isInstanceOf(ConflictException.class).hasMessageContaining("jane@example.com");

            verify(userRepository, never()).save(any());
            verify(jwtService, never()).generateAccessToken(any(), any(), any());
        }

        @Test
        @DisplayName("password is hashed before persistence")
        void register_passwordIsHashed() {
            when(userRepository.existsByEmail(any())).thenReturn(false);
            when(passwordEncoder.encode("SecurePass1!")).thenReturn("bcrypt-hash");
            when(userRepository.save(any(User.class))).thenReturn(activeUser);
            when(jwtService.generateAccessToken(any(), any(), any())).thenReturn("token");
            when(jwtService.getAccessTokenExpirySeconds()).thenReturn(900L);
            when(refreshTokenService.createRefreshToken(any())).thenReturn("refresh");

            authService.register(registerRequest);

            verify(passwordEncoder).encode("SecurePass1!");
            verify(userRepository).save(argThat(u -> u.getPassword().equals("bcrypt-hash")));
        }
    }

    @Nested
    @DisplayName("login()")
    class Login {

        private LoginRequest loginRequest;

        @BeforeEach
        void setUp() {
            loginRequest = LoginRequest.builder().email("jane@example.com").password("SecurePass1!")
                    .build();
        }

        @Test
        @DisplayName("valid credentials → returns token pair")
        void login_success() {
            when(authenticationManager.authenticate(any())).thenReturn(null); // successful auth
            when(userRepository.findByEmail("jane@example.com"))
                    .thenReturn(Optional.of(activeUser));
            when(jwtService.generateAccessToken(any(), eq("user-uuid-123"), eq("USER")))
                    .thenReturn("access-token");
            when(jwtService.getAccessTokenExpirySeconds()).thenReturn(900L);
            when(refreshTokenService.createRefreshToken("user-uuid-123"))
                    .thenReturn("refresh-token");

            AuthResponse response = authService.login(loginRequest);

            assertThat(response.accessToken()).isEqualTo("access-token");
            assertThat(response.userId()).isEqualTo("user-uuid-123");

            verify(authenticationManager).authenticate(
                    argThat(auth -> auth instanceof UsernamePasswordAuthenticationToken
                            && auth.getName().equals("jane@example.com")));
        }

        @Test
        @DisplayName("bad credentials → InvalidCredentialsException (no leak)")
        void login_badCredentials_throwsInvalidCredentials() {
            doThrow(new BadCredentialsException("bad")).when(authenticationManager)
                    .authenticate(any());

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class);

            verify(userRepository, never()).findByEmail(any());
        }

        @Test
        @DisplayName("disabled account → InvalidCredentialsException (no account state leak)")
        void login_disabledAccount_throwsInvalidCredentials() {
            doThrow(new DisabledException("disabled")).when(authenticationManager)
                    .authenticate(any());

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class);
        }
    }

    @Nested
    @DisplayName("refresh()")
    class Refresh {

        private RefreshTokenRequest refreshRequest;

        @BeforeEach
        void setUp() {
            refreshRequest =
                    RefreshTokenRequest.builder().refreshToken("old-refresh-token").build();
        }

        @Test
        @DisplayName("valid refresh token → rotates token and returns new pair")
        void refresh_success() {
            when(refreshTokenService.getUserIdFromRefreshToken("old-refresh-token"))
                    .thenReturn("user-uuid-123");
            when(userRepository.findById("user-uuid-123")).thenReturn(Optional.of(activeUser));
            when(refreshTokenService.rotateRefreshToken("old-refresh-token", "user-uuid-123"))
                    .thenReturn("new-refresh-token");
            when(jwtService.generateAccessToken(any(), eq("user-uuid-123"), eq("USER")))
                    .thenReturn("new-access-token");
            when(jwtService.getAccessTokenExpirySeconds()).thenReturn(900L);

            AuthResponse response = authService.refresh(refreshRequest);

            assertThat(response.accessToken()).isEqualTo("new-access-token");
            assertThat(response.refreshToken()).isEqualTo("new-refresh-token");

            verify(refreshTokenService).rotateRefreshToken("old-refresh-token", "user-uuid-123");
        }

        @Test
        @DisplayName("token not in Redis → TokenRefreshException")
        void refresh_tokenNotFound_throws() {
            when(refreshTokenService.getUserIdFromRefreshToken("old-refresh-token"))
                    .thenReturn(null);

            assertThatThrownBy(() -> authService.refresh(refreshRequest))
                    .isInstanceOf(TokenRefreshException.class)
                    .hasMessageContaining("not found or expired");

            verify(userRepository, never()).findById(any());
        }

        @Test
        @DisplayName("user no longer exists → TokenRefreshException")
        void refresh_userGone_throws() {
            when(refreshTokenService.getUserIdFromRefreshToken("old-refresh-token"))
                    .thenReturn("user-uuid-123");
            when(userRepository.findById("user-uuid-123")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.refresh(refreshRequest))
                    .isInstanceOf(TokenRefreshException.class)
                    .hasMessageContaining("no longer exists");
        }

        @Test
        @DisplayName("disabled user → TokenRefreshException + stale token deleted")
        void refresh_disabledUser_throws() {
            when(refreshTokenService.getUserIdFromRefreshToken("old-refresh-token"))
                    .thenReturn("user-uuid-456");
            when(userRepository.findById("user-uuid-456")).thenReturn(Optional.of(disabledUser));

            assertThatThrownBy(() -> authService.refresh(refreshRequest))
                    .isInstanceOf(TokenRefreshException.class).hasMessageContaining("disabled");

            verify(refreshTokenService).delete("old-refresh-token");
            verify(refreshTokenService, never()).rotateRefreshToken(any(), any());
        }
    }

    @Nested
    @DisplayName("logout()")
    class Logout {

        @Test
        @DisplayName("logout → deletes refresh token from Redis")
        void logout_deletesToken() {
            authService.logout("some-refresh-token");
            verify(refreshTokenService).delete("some-refresh-token");
        }

        @Test
        @DisplayName("logout with unknown token → no exception (idempotent)")
        void logout_unknownToken_noException() {
            doNothing().when(refreshTokenService).delete(any());
            assertThatCode(() -> authService.logout("nonexistent-token"))
                    .doesNotThrowAnyException();
        }
    }
}
