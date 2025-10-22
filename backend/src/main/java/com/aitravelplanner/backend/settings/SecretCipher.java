package com.aitravelplanner.backend.settings;

public interface SecretCipher {

  String encrypt(String plainText);

  String decrypt(String cipherText);
}
