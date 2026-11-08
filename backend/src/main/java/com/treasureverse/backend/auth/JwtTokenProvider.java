package com.treasureverse.backend.auth;

import com.treasureverse.backend.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessSeconds;
    private final long refreshSeconds;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret,
                            @Value("${app.jwt.access-token-seconds}") long accessSeconds,
                            @Value("${app.jwt.refresh-token-seconds}") long refreshSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessSeconds = accessSeconds;
        this.refreshSeconds = refreshSeconds;
    }

    public AuthToken create(Member member) {
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();
        return new AuthToken(token(member, accessJti, accessSeconds, "access"), token(member, refreshJti, refreshSeconds, "refresh"), accessSeconds);
    }

    private String token(Member member, String jti, long seconds, String type) {
        Instant now = Instant.now();
        return Jwts.builder().subject(member.getId().toString()).id(jti).claim("username", member.getUsername())
                .claim("role", member.getRole().name()).claim("type", type).issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(seconds))).signWith(key).compact();
    }

    public Claims parse(String token) { return Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token).getPayload(); }
    public Duration remaining(Claims claims) { return Duration.between(Instant.now(), claims.getExpiration().toInstant()); }
}
