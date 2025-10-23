package com.aitravelplanner.backend.plan.dto;

import com.aitravelplanner.backend.plan.PlanStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TravelPlanSummaryDto {

  UUID id;
  String title;
  List<String> destinations;
  LocalDate startDate;
  LocalDate endDate;
  BigDecimal budgetTotal;
  PlanStatus status;
}
