package com.aitravelplanner.backend.plan;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayPlanRepository extends JpaRepository<DayPlan, UUID> {

  List<DayPlan> findByPlanIdOrderByDayIndex(UUID planId);
}
