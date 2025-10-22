package com.aitravelplanner.backend.settings;

import com.aitravelplanner.backend.user.ApiProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApiKeyRequest(
    @NotNull(message = "provider 不能为空") ApiProvider provider,
    @Size(max = 80, message = "别名长度不可超过 80") String alias,
    @NotBlank(message = "密钥值不能为空") String value) {}
