package com.aitravelplanner.backend.config;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

  /**
   * HMAC 签名密钥，至少 32 字节。生产环境应从安全配置中注入。
   */
  private String secret = "change-me-please-change-me-change-me";

  /**
   * JWT 签发方名称。
   */
  private String issuer = "ai-travel-planner";

  /**
   * 访问令牌有效期。
   */
  private Duration accessTokenTtl = Duration.ofHours(1);

  /**
   * 刷新令牌有效期。
   */
  private Duration refreshTokenTtl = Duration.ofDays(7);
}
