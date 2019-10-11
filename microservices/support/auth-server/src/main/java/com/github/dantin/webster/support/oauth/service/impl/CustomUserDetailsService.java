package com.github.dantin.webster.support.oauth.service.impl;

import com.github.dantin.webster.support.oauth.entity.domain.User;
import com.github.dantin.webster.support.oauth.entity.dto.AuthUser;
import com.github.dantin.webster.support.oauth.repository.UserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

  private final UserMapper userMapper;

  public CustomUserDetailsService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    LOGGER.info("find user by username {}", username);
    Optional<User> existsUser = Optional.ofNullable(userMapper.findOneByUsername(username));
    if (!existsUser.isPresent()) {
      throw new UsernameNotFoundException("username " + username + " not found");
    }
    return AuthUser.builder(existsUser.get()).build();
  }
}
