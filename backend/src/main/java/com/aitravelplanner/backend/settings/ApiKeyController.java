package com.aitravelplanner.backend.settings;

import com.aitravelplanner.backend.user.ApiProvider;
import com.aitravelplanner.backend.user.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings/api-keys")
public class ApiKeyController {

  private final ApiKeyService apiKeyService;

  public ApiKeyController(ApiKeyService apiKeyService) {
    this.apiKeyService = apiKeyService;
  }

  @GetMapping
  public List<ApiKeyResponse> list(@AuthenticationPrincipal UserPrincipal principal) {
    return apiKeyService.listKeys(principal.getId());
  }

  @PostMapping
  public ResponseEntity<ApiKeyResponse> upsert(
      @AuthenticationPrincipal UserPrincipal principal,
      @Valid @RequestBody ApiKeyRequest request) {
    ApiKeyResponse response = apiKeyService.upsertKey(principal.getId(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/{provider}")
  public ResponseEntity<Void> delete(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable("provider") ApiProvider provider) {
    apiKeyService.deleteKey(principal.getId(), provider);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/test")
  public ApiKeyTestResponse test(@Valid @RequestBody ApiKeyTestRequest request) {
    return apiKeyService.testKey(request);
  }
}
