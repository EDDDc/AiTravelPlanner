package com.aitravelplanner.backend.plan;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, UUID> {

  List<TravelPlan> findByUserIdOrderByStartDateDesc(UUID userId);
}
