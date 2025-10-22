package com.aitravelplanner.backend.user;

import com.aitravelplanner.backend.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Column(name = "email", nullable = false, unique = true, length = 180)
  private String email;

  @Column(name = "password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Column(name = "display_name", nullable = false, length = 120)
  private String displayName;

  @Enumerated(EnumType.STRING)
  @Column(name = "auth_provider", nullable = false, length = 20)
  private AuthProvider authProvider = AuthProvider.LOCAL;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @OneToMany(mappedBy = "user")
  private Set<UserSetting> settings = new HashSet<>();
}
