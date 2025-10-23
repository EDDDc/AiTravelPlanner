package com.aitravelplanner.backend.plan;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TravelPlanNotFoundException extends ResponseStatusException {

  public TravelPlanNotFoundException(UUID planId) {
    super(HttpStatus.NOT_FOUND, "找不到指定的行程：" + planId);
  }
}
