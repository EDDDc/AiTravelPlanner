package com.aitravelplanner.backend.plan.dto;

import com.aitravelplanner.backend.plan.ActivityType;
import java.math.BigDecimal;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ActivityDto {

  ActivityType type;
  String name;
  String description;
  LocalTime startTime;
  LocalTime endTime;
  BigDecimal costEstimate;
  Double latitude;
  Double longitude;
  String address;
}
