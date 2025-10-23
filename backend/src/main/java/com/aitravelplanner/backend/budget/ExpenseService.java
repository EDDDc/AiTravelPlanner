package com.aitravelplanner.backend.budget;

import com.aitravelplanner.backend.budget.dto.ExpenseRequest;
import com.aitravelplanner.backend.budget.dto.ExpenseResponse;
import com.aitravelplanner.backend.budget.dto.ExpenseSummaryItem;
import com.aitravelplanner.backend.budget.dto.ExpenseSummaryResponse;
import com.aitravelplanner.backend.plan.TravelPlan;
import com.aitravelplanner.backend.plan.TravelPlanNotFoundException;
import com.aitravelplanner.backend.plan.TravelPlanRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

  private final ExpenseRepository expenseRepository;
  private final TravelPlanRepository travelPlanRepository;

  @Transactional(readOnly = true)
  public List<ExpenseResponse> listExpenses(
      UUID userId, UUID planId, Optional<OffsetDateTime> start, Optional<OffsetDateTime> end) {
    TravelPlan plan = loadPlan(userId, planId);

    List<Expense> expenses;
    if (start.isPresent() && end.isPresent()) {
      expenses =
          expenseRepository.findByPlanIdAndRecordedAtBetweenOrderByRecordedAt(
              plan.getId(), start.get(), end.get());
    } else {
      expenses = expenseRepository.findByPlanIdOrderByRecordedAtDesc(plan.getId());
    }

    return expenses.stream().map(this::toResponse).collect(Collectors.toList());
  }

  @Transactional
  public ExpenseResponse createExpense(UUID userId, UUID planId, ExpenseRequest request) {
    TravelPlan plan = loadPlan(userId, planId);

    Expense expense = new Expense();
    expense.setPlan(plan);
    applyRequest(expense, request);

    Expense saved = expenseRepository.save(expense);
    return toResponse(saved);
  }

  @Transactional
  public ExpenseResponse updateExpense(
      UUID userId, UUID planId, UUID expenseId, ExpenseRequest request) {
    TravelPlan plan = loadPlan(userId, planId);

    Expense expense =
        expenseRepository
            .findById(expenseId)
            .filter(e -> e.getPlan().getId().equals(plan.getId()))
            .orElseThrow(() -> new ExpenseNotFoundException(expenseId));

    applyRequest(expense, request);
    return toResponse(expense);
  }

  @Transactional
  public void deleteExpense(UUID userId, UUID planId, UUID expenseId) {
    TravelPlan plan = loadPlan(userId, planId);

    Expense expense =
        expenseRepository
            .findById(expenseId)
            .filter(e -> e.getPlan().getId().equals(plan.getId()))
            .orElseThrow(() -> new ExpenseNotFoundException(expenseId));

    expenseRepository.delete(expense);
  }

  @Transactional(readOnly = true)
  public ExpenseSummaryResponse summarizeExpenses(
      UUID userId, UUID planId, Optional<OffsetDateTime> start, Optional<OffsetDateTime> end) {
    TravelPlan plan = loadPlan(userId, planId);

    List<Expense> expenses;
    if (start.isPresent() && end.isPresent()) {
      expenses =
          expenseRepository.findByPlanIdAndRecordedAtBetweenOrderByRecordedAt(
              plan.getId(), start.get(), end.get());
    } else {
      expenses = expenseRepository.findByPlanIdOrderByRecordedAtDesc(plan.getId());
    }

    BigDecimal total =
        expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    Map<ExpenseCategory, BigDecimal> perCategory = new EnumMap<>(ExpenseCategory.class);
    for (Expense expense : expenses) {
      perCategory.merge(expense.getCategory(), expense.getAmount(), BigDecimal::add);
    }

    List<ExpenseSummaryItem> items =
        perCategory.entrySet().stream()
            .sorted(Map.Entry.comparingByKey(Comparator.comparing(Enum::name)))
            .map(
                entry ->
                    ExpenseSummaryItem.builder()
                        .category(entry.getKey())
                        .amount(entry.getValue())
                        .build())
            .collect(Collectors.toList());

    String currency =
        expenses.stream()
            .map(Expense::getCurrency)
            .filter(value -> value != null && !value.isBlank())
            .findFirst()
            .orElse("CNY");

    return ExpenseSummaryResponse.builder()
        .currency(currency)
        .totalAmount(total)
        .categories(items)
        .build();
  }

  private TravelPlan loadPlan(UUID userId, UUID planId) {
    return travelPlanRepository
        .findById(planId)
        .filter(plan -> plan.getUser().getId().equals(userId))
        .orElseThrow(() -> new TravelPlanNotFoundException(planId));
  }

  private void applyRequest(Expense expense, ExpenseRequest request) {
    expense.setAmount(request.getAmount());
    expense.setCurrency(request.getCurrency().trim().toUpperCase());
    expense.setCategory(request.getCategory());
    expense.setMethod(request.getMethod());
    expense.setRecordedAt(request.getRecordedAt());
    expense.setNotes(request.getNotes());
    expense.setTranscript(request.getTranscript());
  }

  private ExpenseResponse toResponse(Expense expense) {
    return ExpenseResponse.builder()
        .id(expense.getId())
        .planId(expense.getPlan().getId())
        .amount(expense.getAmount())
        .currency(expense.getCurrency())
        .category(expense.getCategory())
        .method(expense.getMethod())
        .recordedAt(expense.getRecordedAt())
        .notes(expense.getNotes())
        .transcript(expense.getTranscript())
        .createdAt(expense.getCreatedAt())
        .updatedAt(expense.getUpdatedAt())
        .build();
  }
}
