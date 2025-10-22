package com.aitravelplanner.backend.auth;

public record AuthResponse(String accessToken, String refreshToken, UserProfile user) {}
