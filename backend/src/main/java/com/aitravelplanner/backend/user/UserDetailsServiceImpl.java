package com.aitravelplanner.backend.user;

import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(username.toLowerCase())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return toPrincipal(user);
  }

  public UserDetails loadUserById(UUID userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return toPrincipal(user);
  }

  private UserPrincipal toPrincipal(User user) {
    return new UserPrincipal(user.getId(), user.getEmail(), user.getPasswordHash());
  }
}
