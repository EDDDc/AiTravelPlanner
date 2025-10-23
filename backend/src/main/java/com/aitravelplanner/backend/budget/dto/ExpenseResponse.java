package com.aitravelplanner.backend.budget.dto;

import com.aitravelplanner.backend.budget.ExpenseCategory;
import com.aitravelplanner.backend.budget.ExpenseMethod;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExpenseResponse {

  UUID id;
  UUID planId;
  BigDecimal amount;
  String currency;
  ExpenseCategory category;
  ExpenseMethod method;
  OffsetDateTime recordedAt;
  String notes;
  String transcript;
  OffsetDateTime createdAt;
  OffsetDateTime updatedAt;
}
