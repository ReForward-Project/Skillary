package com.example.springskillaryback.common.util;

import com.example.springskillaryback.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenizer {

private final JwtProperties properties;
private final SecretKey secretKey;

public JwtTokenizer(JwtProperties properties) {
    this.properties = properties;
    this.secretKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
}

public String createAccessToken(String subject) {
    Instant now = Instant.now();
    Instant expiresAt = now.plus(properties.getAccessExpiryMinutes(), ChronoUnit.MINUTES);
    return Jwts.builder()
            .subject(subject)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .signWith(secretKey)
            .compact();
}

public String createRefreshToken(String subject) {
    Instant now = Instant.now();
    Instant expiresAt = now.plus(properties.getRefreshExpiryDays(), ChronoUnit.DAYS);
    return Jwts.builder()
            .subject(subject)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .signWith(secretKey)
            .compact();
}

public Claims parseClaims(String token, boolean allowExpired) {
    try {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    } catch (ExpiredJwtException exception) {
        if (allowExpired) {
            return exception.getClaims();
        }
        throw exception;
    }
}
}