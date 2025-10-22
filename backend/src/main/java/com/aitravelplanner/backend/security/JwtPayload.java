package com.aitravelplanner.backend.security;

import java.time.Instant;
import java.util.UUID;

public record JwtPayload(UUID userId, String email, boolean refreshToken, Instant expiresAt) {}
