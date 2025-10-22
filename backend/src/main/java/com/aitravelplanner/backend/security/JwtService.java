package com.aitravelplanner.backend.security;

import com.aitravelplanner.backend.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

@Component
public class JwtService {

  private static final String CLAIM_REFRESH = "refresh";

  private final JwtProperties properties;
  private SecretKey signingKey;

  public JwtService(JwtProperties properties) {
    this.properties = properties;
  }

  @PostConstruct
  void init() {
    byte[] keyBytes = resolveSecret(properties.getSecret());
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(UUID userId, String email) {
    return buildToken(userId, email, false, properties.getAccessTokenTtl());
  }

  public String generateRefreshToken(UUID userId, String email) {
    return buildToken(userId, email, true, properties.getRefreshTokenTtl());
  }

  public Optional<JwtPayload> parseToken(String token) {
    try {
      Claims claims =
          Jwts.parser()
              .verifyWith(signingKey)
              .requireIssuer(properties.getIssuer())
              .build()
              .parseSignedClaims(token)
              .getPayload();

      UUID userId = UUID.fromString(claims.getSubject());
      String email = claims.get("email", String.class);
      boolean refreshToken = Boolean.TRUE.equals(claims.get(CLAIM_REFRESH, Boolean.class));
      Instant expiresAt = claims.getExpiration().toInstant();
      return Optional.of(new JwtPayload(userId, email, refreshToken, expiresAt));
    } catch (JwtException | IllegalArgumentException ex) {
      return Optional.empty();
    }
  }

  private String buildToken(
      UUID userId, String email, boolean refreshToken, java.time.Duration ttl) {
    Instant now = Instant.now();
    Instant expiry = now.plus(ttl);
    return Jwts.builder()
        .issuer(properties.getIssuer())
        .subject(userId.toString())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiry))
        .claim("email", email)
        .claim(CLAIM_REFRESH, refreshToken)
        .signWith(signingKey)
        .compact();
  }

  private byte[] resolveSecret(String secret) {
    byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
    if (secret.trim().startsWith("BASE64:")) {
      return Decoders.BASE64.decode(secret.substring("BASE64:".length()));
    }
    if (raw.length < 32) {
      throw new IllegalStateException("JWT secret must be at least 32 bytes");
    }
    return raw;
  }
}
