package com.aitravelplanner.backend.settings;

import com.aitravelplanner.backend.user.ApiProvider;
import java.util.List;
import java.util.UUID;

public interface ApiKeyService {

  List<ApiKeyResponse> listKeys(UUID userId);

  ApiKeyResponse upsertKey(UUID userId, ApiKeyRequest request);

  void deleteKey(UUID userId, ApiProvider provider);

  ApiKeyTestResponse testKey(ApiKeyTestRequest request);
}
