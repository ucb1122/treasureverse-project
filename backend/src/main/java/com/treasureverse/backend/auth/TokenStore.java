package com.treasureverse.backend.auth;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {
    private final StringRedisTemplate redis;
    public TokenStore(StringRedisTemplate redis) { this.redis = redis; }
    public void saveRefresh(String jti, String userId, Duration ttl) { redis.opsForValue().set("auth:refresh:" + jti, userId, ttl); }
    public boolean consumeRefresh(String jti) { return Boolean.TRUE.equals(redis.delete("auth:refresh:" + jti)); }
    public void blacklistAccess(String jti, Duration ttl) { if (!ttl.isNegative() && !ttl.isZero()) redis.opsForValue().set("auth:blacklist:" + jti, "1", ttl); }
    public boolean isBlacklisted(String jti) { return Boolean.TRUE.equals(redis.hasKey("auth:blacklist:" + jti)); }
}
