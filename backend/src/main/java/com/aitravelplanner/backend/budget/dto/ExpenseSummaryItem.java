package com.aitravelplanner.backend.budget.dto;

import com.aitravelplanner.backend.budget.ExpenseCategory;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExpenseSummaryItem {
  ExpenseCategory category;
  BigDecimal amount;
}
