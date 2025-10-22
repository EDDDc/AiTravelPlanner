package com.aitravelplanner.backend.settings;

import com.aitravelplanner.backend.user.ApiProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApiKeyTestRequest(
    @NotNull(message = "provider 不能为空") ApiProvider provider,
    @NotBlank(message = "密钥值不能为空") String value) {}
