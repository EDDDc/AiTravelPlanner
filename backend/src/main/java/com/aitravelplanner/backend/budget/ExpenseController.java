package com.aitravelplanner.backend.budget;

import com.aitravelplanner.backend.budget.dto.ExpenseRequest;
import com.aitravelplanner.backend.budget.dto.ExpenseResponse;
import com.aitravelplanner.backend.budget.dto.ExpenseSummaryResponse;
import com.aitravelplanner.backend.user.UserPrincipal;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans/{planId}/expenses")
@RequiredArgsConstructor
public class ExpenseController {

  private final ExpenseService expenseService;

  @GetMapping
  public List<ExpenseResponse> listExpenses(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable UUID planId,
      @RequestParam(name = "start", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime start,
      @RequestParam(name = "end", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime end) {
    return expenseService.listExpenses(
        principal.getId(), planId, Optional.ofNullable(start), Optional.ofNullable(end));
  }

  @PostMapping
  public ResponseEntity<ExpenseResponse> createExpense(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable UUID planId,
      @RequestBody @Valid ExpenseRequest request) {
    ExpenseResponse response = expenseService.createExpense(principal.getId(), planId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{expenseId}")
  public ExpenseResponse updateExpense(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable UUID planId,
      @PathVariable UUID expenseId,
      @RequestBody @Valid ExpenseRequest request) {
    return expenseService.updateExpense(principal.getId(), planId, expenseId, request);
  }

  @DeleteMapping("/{expenseId}")
  public ResponseEntity<Void> deleteExpense(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable UUID planId,
      @PathVariable UUID expenseId) {
    expenseService.deleteExpense(principal.getId(), planId, expenseId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/summary")
  public ExpenseSummaryResponse summarizeExpenses(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable UUID planId,
      @RequestParam(name = "start", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime start,
      @RequestParam(name = "end", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          OffsetDateTime end) {
    return expenseService.summarizeExpenses(
        principal.getId(), planId, Optional.ofNullable(start), Optional.ofNullable(end));
  }
}
