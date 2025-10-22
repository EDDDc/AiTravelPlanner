package com.aitravelplanner.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCredentialsException extends ResponseStatusException {

  public InvalidCredentialsException() {
    super(HttpStatus.UNAUTHORIZED, "邮箱或密码错误");
  }
}
