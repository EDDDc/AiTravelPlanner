package com.aitravelplanner.backend.budget.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExpenseSummaryResponse {
  BigDecimal totalAmount;
  String currency;
  List<ExpenseSummaryItem> categories;
}
