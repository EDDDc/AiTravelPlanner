package com.aitravelplanner.backend.budget;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

  List<Expense> findByPlanIdOrderByRecordedAtDesc(UUID planId);

  List<Expense> findByPlanIdAndRecordedAtBetweenOrderByRecordedAt(
      UUID planId, OffsetDateTime start, OffsetDateTime end);
}
