package com.github.dantin.webster.support.oauth.service.impl;

import com.github.dantin.webster.support.oauth.entity.domain.User;
import com.github.dantin.webster.support.oauth.repository.UserRepository;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOGGER.info("find user by username {}", username);
    User user = userRepository.findByUsername(username);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("username " + username + " not found");
    }
    return user;
  }
}
