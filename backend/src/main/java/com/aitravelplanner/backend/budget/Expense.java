package com.aitravelplanner.backend.budget;

import com.aitravelplanner.backend.common.model.BaseEntity;
import com.aitravelplanner.backend.plan.TravelPlan;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "expenses")
public class Expense extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "plan_id", nullable = false)
  private TravelPlan plan;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false, length = 30)
  private ExpenseCategory category = ExpenseCategory.OTHER;

  @Column(name = "amount", nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(name = "currency", nullable = false, length = 10)
  private String currency = "CNY";

  @Enumerated(EnumType.STRING)
  @Column(name = "method", nullable = false, length = 20)
  private ExpenseMethod method = ExpenseMethod.MANUAL;

  @Column(name = "transcript", columnDefinition = "text")
  private String transcript;

  @Column(name = "recorded_at", nullable = false)
  private OffsetDateTime recordedAt;

  @Column(name = "notes", columnDefinition = "text")
  private String notes;
}
