package com.github.dantin.webster.support.oauth.service.impl;

import com.github.dantin.webster.support.oauth.entity.domain.User;
import com.github.dantin.webster.support.oauth.repository.UserMapper;
import com.github.dantin.webster.support.oauth.service.UserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserServiceImpl(PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  @Override
  public void create(User user) {
    throwIfUsernameExists(user.getUsername());

    LOGGER.info("create user {}", user.getUsername());
    String hash = passwordEncoder.encode(user.getPassword());
    user.setUsername(user.getUsername());
    user.setPassword(hash);
    // user.setActivated(Boolean.TRUE); // TODO: send sms or email with code for activation
    // user.setAuthorities(CollectionsHelper.setOf(Authorities.ROLE_USER));

    // TODO: other routines on account creation

    userMapper.save(user);
  }

  private void throwIfUsernameExists(String username) {
    Optional<User> existingUser = Optional.ofNullable(userMapper.findByUsername(username));
    existingUser.ifPresent(
        (user) -> {
          throw new IllegalArgumentException("user already exists");
        });
  }
}
