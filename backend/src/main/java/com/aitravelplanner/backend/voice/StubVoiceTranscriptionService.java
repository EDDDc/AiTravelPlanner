package com.aitravelplanner.backend.voice;

import com.aitravelplanner.backend.user.UserPrincipal;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class StubVoiceTranscriptionService implements VoiceTranscriptionService {

  private static final int MAX_SIZE_BYTES = 5 * 1024 * 1024;

  @Override
  public TranscriptionResponse transcribe(UserPrincipal principal, MultipartFile audioFile) {
    if (audioFile.isEmpty()) {
      throw new IllegalArgumentException("上传的音频文件为空");
    }
    if (audioFile.getSize() > MAX_SIZE_BYTES) {
      throw new IllegalArgumentException("请上传不超过 5MB 的音频文件");
    }

    log.info(
        "Voice transcription request received. userId={}, filename={}, size={}",
        principal.getId(),
        audioFile.getOriginalFilename(),
        audioFile.getSize());

    String transcript =
        "【占位转写】"
            + "收到 "
            + audioFile.getOriginalFilename()
            + "，长度约 "
            + audioFile.getSize()
            + " 字节。"
            + "语音识别接入科大讯飞服务后将在此返回真实文本。";

    return TranscriptionResponse.builder()
        .transcript(transcript)
        .provider("XFYUN")
        .status("PENDING_PROVIDER_INTEGRATION")
        .build();
  }
}
