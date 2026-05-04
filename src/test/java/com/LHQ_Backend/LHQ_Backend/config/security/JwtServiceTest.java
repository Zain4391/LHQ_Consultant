package com.LHQ_Backend.LHQ_Backend.config.security;

import com.LHQ_Backend.LHQ_Backend.user.entity.User;
import com.LHQ_Backend.LHQ_Backend.user.enums.Role;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {

    // 32+ char secret required for HS256
    private static final String SECRET = "test-secret-key-that-is-long-enough-for-hs256!!";
    private static final long EXPIRY_SECONDS = 900L;

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, EXPIRY_SECONDS);

        user = User.builder().id("user-uuid-123").email("jane@example.com").role(Role.LAWYER)
                .isActive(true).build();
    }

    @Test
    @DisplayName("generated token is parseable and contains correct claims")
    void generateToken_claimsAreCorrect() {
        String token = jwtService.generateAccessToken(user, user.getId(), user.getRole().name());

        assertThat(jwtService.extractSubject(token)).isEqualTo("user-uuid-123");
        assertThat(jwtService.extractEmail(token)).isEqualTo("jane@example.com");
        assertThat(jwtService.extractRole(token)).isEqualTo("LAWYER");
    }

    @Test
    @DisplayName("valid token passes validation")
    void isTokenValid_validToken_returnsTrue() {
        String token = jwtService.generateAccessToken(user, user.getId(), user.getRole().name());
        assertThat(jwtService.isTokenValid(token, user, user.getId())).isTrue();
    }

    @Test
    @DisplayName("token with wrong userId fails validation")
    void isTokenValid_wrongSubject_returnsFalse() {
        String token = jwtService.generateAccessToken(user, user.getId(), user.getRole().name());
        assertThat(jwtService.isTokenValid(token, user, "different-user-id")).isFalse();
    }

    @Test
    @DisplayName("expired token fails validation")
    void isTokenValid_expiredToken_returnsFalse() {
        JwtService shortLivedService = new JwtService(SECRET, -1L); // already expired
        String token =
                shortLivedService.generateAccessToken(user, user.getId(), user.getRole().name());
        assertThat(jwtService.isTokenValid(token, user, user.getId())).isFalse();
    }

    @Test
    @DisplayName("tampered token throws JwtException on extraction")
    void extractSubject_tamperedToken_throwsJwtException() {
        String token = jwtService.generateAccessToken(user, user.getId(), user.getRole().name());
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        assertThatThrownBy(() -> jwtService.extractSubject(tampered))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("expiry is set correctly within acceptable delta")
    void expiration_isWithinExpectedRange() {
        Instant before = Instant.now();
        String token = jwtService.generateAccessToken(user, user.getId(), user.getRole().name());
        Instant expiry = jwtService.extractExpiration(token);

        assertThat(expiry).isAfter(before.plusSeconds(EXPIRY_SECONDS - 5));
        assertThat(expiry).isBefore(before.plusSeconds(EXPIRY_SECONDS + 5));
    }

    @Test
    @DisplayName("token signed with different secret fails validation")
    void validateToken_differentSecret_returnsFalse() {
        JwtService otherService =
                new JwtService("completely-different-secret-key-32chars!!", EXPIRY_SECONDS);
        String tokenFromOther =
                otherService.generateAccessToken(user, user.getId(), user.getRole().name());

        // Our service cannot validate a token signed by a different key
        assertThatThrownBy(() -> jwtService.extractSubject(tokenFromOther))
                .isInstanceOf(JwtException.class);
    }
}
