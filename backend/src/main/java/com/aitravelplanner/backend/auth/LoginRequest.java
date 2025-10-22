package com.aitravelplanner.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @Email(message = "邮箱格式不正确") String email,
    @NotBlank(message = "密码不能为空") @Size(min = 8, max = 64) String password) {}
