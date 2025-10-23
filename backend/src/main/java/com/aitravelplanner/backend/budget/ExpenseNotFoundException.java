package com.aitravelplanner.backend.budget;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExpenseNotFoundException extends ResponseStatusException {

  public ExpenseNotFoundException(UUID expenseId) {
    super(HttpStatus.NOT_FOUND, "未找到指定的费用记录：" + expenseId);
  }
}
