package com.aitravelplanner.backend.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aitravelplanner.backend.auth.LoginRequest;
import com.aitravelplanner.backend.auth.RegisterRequest;
import com.aitravelplanner.backend.budget.dto.ExpenseRequest;
import com.aitravelplanner.backend.plan.dto.TravelPlanCreateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExpenseControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private String jwtToken;
  private UUID planId;

  @BeforeEach
  void setUp() throws Exception {
    String email = "budget_" + UUID.randomUUID() + "@example.com";
    RegisterRequest registerRequest =
        new RegisterRequest(email, "StrongPass123!", "budget tester");

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated());

    LoginRequest loginRequest = new LoginRequest(email, "StrongPass123!");
    String loginResponse =
        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    JsonNode loginJson = objectMapper.readTree(loginResponse);
    jwtToken = loginJson.get("accessToken").asText();

    TravelPlanCreateRequest planRequest =
        TravelPlanCreateRequest.builder()
            .title("Budget Test Plan")
            .destinations(List.of("Shanghai"))
            .startDate(java.time.LocalDate.of(2025, 5, 1))
            .endDate(java.time.LocalDate.of(2025, 5, 3))
            .build();

    String planResponse =
        mockMvc
            .perform(
                post("/api/plans")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(planRequest)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    planId = UUID.fromString(objectMapper.readTree(planResponse).get("id").asText());
  }

  @Test
  @DisplayName("create and list expenses")
  void shouldCreateAndListExpenses() throws Exception {
    ExpenseRequest request =
        ExpenseRequest.builder()
            .amount(new BigDecimal("120.50"))
            .currency("cny")
            .category(ExpenseCategory.DINING)
            .method(ExpenseMethod.MANUAL)
            .recordedAt(OffsetDateTime.now(ZoneOffset.UTC))
            .notes("Test dining expense")
            .build();

    mockMvc
        .perform(
            post("/api/plans/{planId}/expenses", planId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.currency").value("CNY"))
        .andExpect(jsonPath("$.amount").value(120.50));

    mockMvc
        .perform(
            get("/api/plans/{planId}/expenses", planId)
                .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].category").value("DINING"));
  }

  @Test
  @DisplayName("update delete and summarize expenses")
  void shouldUpdateDeleteAndSummarize() throws Exception {
    OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
    ExpenseRequest createRequest =
        ExpenseRequest.builder()
            .amount(new BigDecimal("500"))
            .currency("USD")
            .category(ExpenseCategory.ACCOMMODATION)
            .method(ExpenseMethod.MANUAL)
            .recordedAt(now)
            .notes("Hotel stay")
            .build();

    MvcResult createResult =
        mockMvc
            .perform(
                post("/api/plans/{planId}/expenses", planId)
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn();

    JsonNode createdJson =
        objectMapper.readTree(
            createResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    UUID expenseId = UUID.fromString(createdJson.get("id").asText());

    ExpenseRequest updateRequest =
        ExpenseRequest.builder()
            .amount(new BigDecimal("650.75"))
            .currency("USD")
            .category(ExpenseCategory.ACCOMMODATION)
            .method(ExpenseMethod.MANUAL)
            .recordedAt(now.plusHours(2))
            .notes("Room upgrade")
            .build();

    mockMvc
        .perform(
            put("/api/plans/{planId}/expenses/{expenseId}", planId, expenseId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.amount").value(650.75));

    String summaryResponse =
        mockMvc
            .perform(
                get("/api/plans/{planId}/expenses/summary", planId)
                    .header("Authorization", "Bearer " + jwtToken))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    JsonNode summaryJson = objectMapper.readTree(summaryResponse);
    assertThat(summaryJson.get("totalAmount").decimalValue()).isEqualByComparingTo("650.75");

    mockMvc
        .perform(
            delete("/api/plans/{planId}/expenses/{expenseId}", planId, expenseId)
                .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(
            get("/api/plans/{planId}/expenses", planId)
                .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  @DisplayName("filter expenses by date range and compute summary")
  void shouldFilterByDateRange() throws Exception {
    OffsetDateTime base = OffsetDateTime.now(ZoneOffset.UTC).withNano(0);

    ExpenseRequest transport =
        ExpenseRequest.builder()
            .amount(new BigDecimal("200"))
            .currency("CNY")
            .category(ExpenseCategory.TRANSPORT)
            .method(ExpenseMethod.MANUAL)
            .recordedAt(base.minusDays(1))
            .build();

    ExpenseRequest dining =
        ExpenseRequest.builder()
            .amount(new BigDecimal("80"))
            .currency("CNY")
            .category(ExpenseCategory.DINING)
            .method(ExpenseMethod.MANUAL)
            .recordedAt(base)
            .build();

    ExpenseRequest shopping =
        ExpenseRequest.builder()
            .amount(new BigDecimal("150"))
            .currency("CNY")
            .category(ExpenseCategory.SHOPPING)
            .method(ExpenseMethod.MANUAL)
            .recordedAt(base.plusDays(1))
            .build();

    for (ExpenseRequest request : List.of(transport, dining, shopping)) {
      mockMvc
          .perform(
              post("/api/plans/{planId}/expenses", planId)
                  .header("Authorization", "Bearer " + jwtToken)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isCreated());
    }

    String summaryResponse =
        mockMvc
            .perform(
                get("/api/plans/{planId}/expenses/summary", planId)
                    .header("Authorization", "Bearer " + jwtToken)
                    .param("start", base.minusHours(12).toString())
                    .param("end", base.plusHours(12).toString()))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    JsonNode summaryJson = objectMapper.readTree(summaryResponse);
    assertThat(summaryJson.get("totalAmount").decimalValue()).isEqualByComparingTo("80");
    assertThat(summaryJson.get("categories")).hasSize(1);
    assertThat(summaryJson.get("categories").get(0).get("category").asText()).isEqualTo("DINING");
  }
}
