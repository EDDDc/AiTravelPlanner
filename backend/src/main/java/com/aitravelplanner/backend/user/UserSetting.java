package com.aitravelplanner.backend.user;

import com.aitravelplanner.backend.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_settings")
public class UserSetting extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "provider", nullable = false, length = 30)
  private ApiProvider provider;

  @Column(name = "key_alias", nullable = false, length = 80)
  private String keyAlias;

  @Column(name = "encrypted_key", nullable = false, columnDefinition = "text")
  private String encryptedKey;
}
