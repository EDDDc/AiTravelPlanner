package com.aitravelplanner.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyUsedException extends ResponseStatusException {

  public EmailAlreadyUsedException() {
    super(HttpStatus.CONFLICT, "邮箱已被使用");
  }
}
