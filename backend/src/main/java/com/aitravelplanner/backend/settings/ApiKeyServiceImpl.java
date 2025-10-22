package com.aitravelplanner.backend.settings;

import com.aitravelplanner.backend.user.ApiProvider;
import com.aitravelplanner.backend.user.User;
import com.aitravelplanner.backend.user.UserNotFoundException;
import com.aitravelplanner.backend.user.UserRepository;
import com.aitravelplanner.backend.user.UserSetting;
import com.aitravelplanner.backend.user.UserSettingRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService {

  private final UserRepository userRepository;
  private final UserSettingRepository userSettingRepository;
  private final SecretCipher secretCipher;

  public ApiKeyServiceImpl(
      UserRepository userRepository,
      UserSettingRepository userSettingRepository,
      SecretCipher secretCipher) {
    this.userRepository = userRepository;
    this.userSettingRepository = userSettingRepository;
    this.secretCipher = secretCipher;
  }

  @Override
  public List<ApiKeyResponse> listKeys(UUID userId) {
    return userSettingRepository.findByUserId(userId).stream()
        .map(
            setting ->
                new ApiKeyResponse(
                    setting.getProvider(), setting.getKeyAlias(), setting.getUpdatedAt()))
        .toList();
  }

  @Override
  public ApiKeyResponse upsertKey(UUID userId, ApiKeyRequest request) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

    UserSetting setting =
        userSettingRepository
            .findByUserIdAndProvider(userId, request.provider())
            .orElseGet(
                () -> {
                  UserSetting created = new UserSetting();
                  created.setUser(user);
                  created.setProvider(request.provider());
                  return created;
                });

    String alias =
        StringUtils.hasText(request.alias())
            ? request.alias()
            : request.provider().name().toLowerCase(Locale.ROOT) + "-key";

    setting.setKeyAlias(alias);
    setting.setEncryptedKey(secretCipher.encrypt(request.value()));

    UserSetting saved = userSettingRepository.save(setting);
    return new ApiKeyResponse(saved.getProvider(), saved.getKeyAlias(), saved.getUpdatedAt());
  }

  @Override
  public void deleteKey(UUID userId, ApiProvider provider) {
    userSettingRepository
        .findByUserIdAndProvider(userId, provider)
        .ifPresent(userSettingRepository::delete);
  }

  @Override
  public ApiKeyTestResponse testKey(ApiKeyTestRequest request) {
    boolean success = request.value().trim().length() >= 10;
    String message =
        success
            ? "密钥格式初步校验通过，实际连通性将在保存后通过真实调用验证。"
            : "密钥长度不符合预期，请检查配置。";
    return new ApiKeyTestResponse(success, message);
  }
}
