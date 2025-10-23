package com.aitravelplanner.backend.plan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aitravelplanner.backend.auth.LoginRequest;
import com.aitravelplanner.backend.auth.RegisterRequest;
import com.aitravelplanner.backend.plan.dto.TravelPlanCreateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
class TravelPlanControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private String jwtToken;

  @BeforeEach
  void setupUser() throws Exception {
    String email = "planner_" + UUID.randomUUID() + "@example.com";
    RegisterRequest registerRequest =
        new RegisterRequest(email, "StrongPass123!", "integration tester");

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated());

    LoginRequest loginRequest = new LoginRequest(email, "StrongPass123!");
    MvcResult loginResult =
        mockMvc
            .perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();

    JsonNode loginJson =
        objectMapper.readTree(loginResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    jwtToken = loginJson.get("accessToken").asText();
  }

  @Test
  @DisplayName("create plan then retrieve details")
  void shouldCreatePlanAndRetrieveDetail() throws Exception {
    TravelPlanCreateRequest request =
        TravelPlanCreateRequest.builder()
            .title("Tokyo Family Trip")
            .destinations(List.of("Tokyo"))
            .startDate(LocalDate.of(2025, 2, 1))
            .endDate(LocalDate.of(2025, 2, 3))
            .budgetTotal(new BigDecimal("15000"))
            .preferences(
                Map.of(
                    "travelers", List.of("亲子家庭"),
                    "interests", List.of("美食探索")))
            .build();

    MvcResult createResult =
        mockMvc
            .perform(
                post("/api/plans")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Tokyo Family Trip"))
            .andExpect(jsonPath("$.budgetTotal").value(15000))
            .andExpect(jsonPath("$.preferences.travelers").value("亲子家庭"))
            .andExpect(jsonPath("$.days").isArray())
            .andReturn();

    JsonNode createdJson =
        objectMapper.readTree(createResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    String planId = createdJson.get("id").asText();

    mockMvc
        .perform(
            get("/api/plans")
                .header("Authorization", "Bearer " + jwtToken)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(planId))
        .andExpect(jsonPath("$[0].budgetTotal").value(15000));

    MvcResult detailResult =
        mockMvc
            .perform(
                get("/api/plans/{planId}", planId)
                    .header("Authorization", "Bearer " + jwtToken)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(planId))
            .andExpect(jsonPath("$.preferences.travelers").value("亲子家庭"))
            .andExpect(jsonPath("$.preferences.interests").value("美食探索"))
            .andExpect(jsonPath("$.days").isArray())
            .andReturn();

    JsonNode detailJson =
        objectMapper.readTree(detailResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    assertThat(detailJson.get("days").isArray()).isTrue();
  }

  @Test
  @DisplayName("different users cannot access each other's plan")
  void shouldForbidAccessToOthersPlan() throws Exception {
    TravelPlanCreateRequest request =
        TravelPlanCreateRequest.builder()
            .title("Tokyo Solo Trip")
            .destinations(List.of("Tokyo"))
            .startDate(LocalDate.of(2025, 3, 1))
            .endDate(LocalDate.of(2025, 3, 2))
            .build();

    MvcResult createResult =
        mockMvc
            .perform(
                post("/api/plans")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();

    String planId =
        objectMapper
            .readTree(createResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
            .get("id")
            .asText();

    RegisterRequest otherRegister =
        new RegisterRequest("other_" + UUID.randomUUID() + "@example.com", "StrongPass123!", "other user");
    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otherRegister)))
        .andExpect(status().isCreated());
    LoginRequest otherLogin = new LoginRequest(otherRegister.email(), "StrongPass123!");
    JsonNode otherLoginJson =
        objectMapper.readTree(
            mockMvc
                .perform(
                    post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otherLogin)))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8));
    String otherToken = otherLoginJson.get("accessToken").asText();

    mockMvc
        .perform(
            get("/api/plans/{planId}", planId)
                .header("Authorization", "Bearer " + otherToken)
                .accept(MediaType.APPLICATION_JSON))
        
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("reject plan when end date precedes start date")
  void shouldRejectPlanWithInvalidDateRange() throws Exception {
    TravelPlanCreateRequest request =
        TravelPlanCreateRequest.builder()
            .title("Invalid Date Trip")
            .destinations(List.of("Shanghai"))
            .startDate(LocalDate.of(2025, 5, 10))
            .endDate(LocalDate.of(2025, 5, 8))
            .build();

    mockMvc
        .perform(
            post("/api/plans")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.dateRangeValid").value("结束日期不能早于开始日期"));
  }
}
