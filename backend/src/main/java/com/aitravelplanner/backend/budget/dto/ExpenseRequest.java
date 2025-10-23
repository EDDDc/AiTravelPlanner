package com.aitravelplanner.backend.budget.dto;

import com.aitravelplanner.backend.budget.ExpenseCategory;
import com.aitravelplanner.backend.budget.ExpenseMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExpenseRequest {

  @NotNull(message = "费用金额不能为空")
  @DecimalMin(value = "0.00", inclusive = false, message = "费用金额必须大于 0")
  BigDecimal amount;

  @NotBlank(message = "币种不能为空")
  @Size(max = 10, message = "币种长度不能超过 10")
  String currency;

  @NotNull(message = "费用分类不能为空")
  ExpenseCategory category;

  @NotNull(message = "录入方式不能为空")
  ExpenseMethod method;

  @NotNull(message = "记录时间不能为空")
  OffsetDateTime recordedAt;

  @Size(max = 2000, message = "备注长度不能超过 2000")
  String notes;

  @Size(max = 4000, message = "语音转写长度不能超过 4000")
  String transcript;
}
