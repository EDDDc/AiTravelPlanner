package com.aitravelplanner.backend.settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "test"})
public class NoopSecretCipher implements SecretCipher {

  private static final Logger log = LoggerFactory.getLogger(NoopSecretCipher.class);

  @Override
  public String encrypt(String plainText) {
    log.warn("使用 NoopSecretCipher，仅用于开发环境，生产环境请替换为安全实现");
    return plainText;
  }

  @Override
  public String decrypt(String cipherText) {
    return cipherText;
  }
}
