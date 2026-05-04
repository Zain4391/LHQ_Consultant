package com.LHQ_Backend.LHQ_Backend.config.security;

import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    /*
     * Key Schema: "refresh:{token_uuid}" TTL = refresh token expiry
     */
    private final String KEY_PREFIX = "refresh:";
    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.refresh-expiration:604800}")
    private long refreshTokenExpirySeconds;

    private String key(String token) {
        return KEY_PREFIX + token;
    }

    /* Refresh tokens are UUIDs instead of JWTs */
    public String createRefreshToken(String userId) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key(token), userId,
                Duration.ofSeconds(refreshTokenExpirySeconds));

        log.debug("Refresh token generated for userId={}", userId);
        return token;
    }

    public String getUserIdFromRefreshToken(String token) {
        return redisTemplate.opsForValue().get(key(token));
    }

    public void delete(String token) {
        redisTemplate.delete(key(token));
        log.debug("Refresh token deleted");
    }

    public long getRefreshTokenExpirySeconds() {
        return refreshTokenExpirySeconds;
    }

    public String rotateRefreshToken(String oldToken, String userId) {
        delete(oldToken);
        return createRefreshToken(userId);
    }

}
