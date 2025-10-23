package com.aitravelplanner.backend.plan;

import com.aitravelplanner.backend.plan.dto.TravelPlanCreateRequest;
import com.aitravelplanner.backend.plan.dto.TravelPlanDetailDto;
import com.aitravelplanner.backend.plan.dto.TravelPlanSummaryDto;
import com.aitravelplanner.backend.user.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class TravelPlanController {

  private final TravelPlanService travelPlanService;

  @GetMapping
  public List<TravelPlanSummaryDto> listPlans(@AuthenticationPrincipal UserPrincipal principal) {
    return travelPlanService.listPlans(principal.getId());
  }

  @GetMapping("/{planId}")
  public TravelPlanDetailDto getPlan(
      @AuthenticationPrincipal UserPrincipal principal, @PathVariable UUID planId) {
    return travelPlanService.getPlan(principal.getId(), planId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TravelPlanDetailDto createPlan(
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestBody @Valid TravelPlanCreateRequest request) {
    return travelPlanService.createPlan(principal.getId(), request);
  }
}
