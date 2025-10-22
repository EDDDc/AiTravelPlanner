package com.aitravelplanner.backend.auth;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserProfile(
    UUID id,
    String email,
    String displayName,
    String avatarUrl,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {}
