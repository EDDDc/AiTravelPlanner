package com.aitravelplanner.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Email(message = "邮箱格式不正确") String email,
    @NotBlank(message = "密码不能为空") @Size(min = 8, max = 64, message = "密码长度需在 8-64 之间")
        String password,
    @NotBlank(message = "昵称不能为空") @Size(max = 120, message = "昵称长度不可超过 120") String displayName) {}
