package com.aitravelplanner.backend.plan.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DayPlanDto {

  Integer dayIndex;
  LocalDate date;
  String summary;
  String notes;
  List<ActivityDto> activities;
}
