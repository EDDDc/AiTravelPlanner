package com.aitravelplanner.backend.settings;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aitravelplanner.backend.auth.LoginRequest;
import com.aitravelplanner.backend.auth.RegisterRequest;
import com.aitravelplanner.backend.user.ApiProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class ApiKeyControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private String accessToken;

  @BeforeEach
  void setUp() throws Exception {
    String email = "keyuser_" + UUID.randomUUID() + "@example.com";
    RegisterRequest registerRequest = new RegisterRequest(email, "StrongPass123!", "测试用户");
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

    JsonNode node = objectMapper.readTree(loginResult.getResponse().getContentAsString());
    this.accessToken = node.get("accessToken").asText();
  }

  @Test
  @DisplayName("用户可以增删查自己的 API Key")
  void shouldManageApiKeys() throws Exception {
    mockMvc
        .perform(
            get("/api/settings/api-keys")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));

    ApiKeyRequest keyRequest =
        new ApiKeyRequest(ApiProvider.BAILIAN, "bailian-default", "mock-bailian-key-123456");

    mockMvc
        .perform(
            post("/api/settings/api-keys")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(keyRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.provider").value("BAILIAN"))
        .andExpect(jsonPath("$.alias").value("bailian-default"));

    mockMvc
        .perform(
            get("/api/settings/api-keys")
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));

    mockMvc
        .perform(
            delete("/api/settings/api-keys/{provider}", "BAILIAN")
                .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(
            get("/api/settings/api-keys")
                .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  @DisplayName("测试 API Key 接口返回可读消息")
  void shouldTestKey() throws Exception {
    ApiKeyTestRequest request =
        new ApiKeyTestRequest(ApiProvider.AMAP, "demo-key-1234567890");

    mockMvc
        .perform(
            post("/api/settings/api-keys/test")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }
}
