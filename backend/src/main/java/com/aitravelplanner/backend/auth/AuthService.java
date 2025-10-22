package com.aitravelplanner.backend.auth;

import com.aitravelplanner.backend.security.JwtService;
import com.aitravelplanner.backend.user.AuthProvider;
import com.aitravelplanner.backend.user.User;
import com.aitravelplanner.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    String normalizedEmail = request.email().toLowerCase(Locale.ROOT);
    if (userRepository.existsByEmail(normalizedEmail)) {
      throw new EmailAlreadyUsedException();
    }

    User user = new User();
    user.setEmail(normalizedEmail);
    user.setPasswordHash(passwordEncoder.encode(request.password()));
    user.setDisplayName(request.displayName());
    user.setAuthProvider(AuthProvider.LOCAL);

    User saved = userRepository.save(user);
    return buildAuthResponse(saved);
  }

  public AuthResponse login(LoginRequest request) {
    String normalizedEmail = request.email().toLowerCase(Locale.ROOT);
    User user =
        userRepository
            .findByEmail(normalizedEmail)
            .orElseThrow(InvalidCredentialsException::new);

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new InvalidCredentialsException();
    }
    return buildAuthResponse(user);
  }

  private AuthResponse buildAuthResponse(User user) {
    UserProfile profile = toProfile(user);
    String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
    String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());
    return new AuthResponse(accessToken, refreshToken, profile);
  }

  private UserProfile toProfile(User user) {
    return new UserProfile(
        user.getId(),
        user.getEmail(),
        user.getDisplayName(),
        StringUtils.hasText(user.getAvatarUrl()) ? user.getAvatarUrl() : null,
        user.getCreatedAt(),
        user.getUpdatedAt());
  }
}
