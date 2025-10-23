package com.aitravelplanner.backend.voice;

import com.aitravelplanner.backend.user.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface VoiceTranscriptionService {

  TranscriptionResponse transcribe(UserPrincipal principal, MultipartFile audioFile);
}
