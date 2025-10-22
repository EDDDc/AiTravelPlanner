package com.aitravelplanner.backend.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, UUID> {

  List<UserSetting> findByUserId(UUID userId);

  Optional<UserSetting> findByUserIdAndProvider(UUID userId, ApiProvider provider);
}
