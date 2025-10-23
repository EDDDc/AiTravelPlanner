package com.aitravelplanner.backend.voice;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TranscriptionResponse {

  String transcript;
  String provider;
  String status;
}
