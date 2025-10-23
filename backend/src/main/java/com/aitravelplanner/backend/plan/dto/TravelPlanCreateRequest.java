package com.aitravelplanner.backend.plan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TravelPlanCreateRequest {

  @NotBlank(message = "行程标题不能为空")
  String title;

  @NotNull(message = "至少需要一个目的地")
  List<@NotBlank(message = "目的地不能为空") String> destinations;

  @NotNull(message = "开始日期不能为空")
  LocalDate startDate;

  @NotNull(message = "结束日期不能为空")
  LocalDate endDate;

  BigDecimal budgetTotal;

  Map<String, Object> preferences;
}
