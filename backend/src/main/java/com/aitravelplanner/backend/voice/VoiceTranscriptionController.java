package com.aitravelplanner.backend.voice;

import com.aitravelplanner.backend.user.UserPrincipal;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/voice")
@RequiredArgsConstructor
public class VoiceTranscriptionController {

  private final VoiceTranscriptionService voiceTranscriptionService;

  @PostMapping(
      value = "/transcriptions",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TranscriptionResponse> transcribe(
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestParam("file") @NotNull MultipartFile audioFile) {

    try {
      TranscriptionResponse response = voiceTranscriptionService.transcribe(principal, audioFile);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }
  }
}
