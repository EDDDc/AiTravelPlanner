package com.aitravelplanner.backend.user;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

  public UserNotFoundException(UUID userId) {
    super(HttpStatus.NOT_FOUND, "用户不存在: " + userId);
  }
}
