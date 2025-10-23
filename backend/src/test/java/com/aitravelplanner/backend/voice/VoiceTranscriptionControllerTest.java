package com.aitravelplanner.backend.voice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aitravelplanner.backend.auth.LoginRequest;
import com.aitravelplanner.backend.auth.RegisterRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VoiceTranscriptionControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  private String jwtToken;

  @BeforeEach
  void setupUser() throws Exception {
    String email = "voice_" + UUID.randomUUID() + "@example.com";
    RegisterRequest registerRequest =
        new RegisterRequest(email, "StrongPass123!", "voice tester");

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated());

    LoginRequest loginRequest = new LoginRequest(email, "StrongPass123!");
    String loginResponse =
        mockMvc
            .perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    JsonNode loginJson = objectMapper.readTree(loginResponse);
    jwtToken = loginJson.get("accessToken").asText();
  }

  @Test
  @DisplayName("upload audio file returns placeholder transcription")
  void shouldReturnPlaceholderTranscription() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "sample.wav",
            "audio/wav",
            new byte[] {1, 2, 3, 4, 5});

    mockMvc
        .perform(
            multipart("/api/voice/transcriptions")
                .file(file)
                .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.provider").value("XFYUN"))
        .andExpect(jsonPath("$.status").value("PENDING_PROVIDER_INTEGRATION"))
        .andExpect(jsonPath("$.transcript").value(org.hamcrest.Matchers.containsString("占位转写")));
  }

  @Test
  @DisplayName("reject empty audio upload")
  void shouldRejectEmptyUpload() throws Exception {
    MockMultipartFile empty = new MockMultipartFile("file", "empty.wav", "audio/wav", new byte[0]);

    mockMvc
        .perform(
            multipart("/api/voice/transcriptions")
                .file(empty)
                .header("Authorization", "Bearer " + jwtToken))
        .andExpect(status().isBadRequest());
  }
}
