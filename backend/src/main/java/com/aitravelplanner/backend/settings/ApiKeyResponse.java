package com.aitravelplanner.backend.settings;

import com.aitravelplanner.backend.user.ApiProvider;
import java.time.OffsetDateTime;

public record ApiKeyResponse(ApiProvider provider, String alias, OffsetDateTime updatedAt) {}
