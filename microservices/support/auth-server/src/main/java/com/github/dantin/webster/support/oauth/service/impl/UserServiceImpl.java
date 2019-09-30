package com.github.dantin.webster.support.oauth.service.impl;

import com.github.dantin.webster.common.base.CollectionsHelper;
import com.github.dantin.webster.support.oauth.entity.domain.User;
import com.github.dantin.webster.support.oauth.entity.enums.Authorities;
import com.github.dantin.webster.support.oauth.repository.UserRepository;
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
  private final UserRepository userRepository;

  public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public User create(User user) {
    throwIfUsernameExists(user.getUsername());

    LOGGER.info("create user {}", user.getUsername());
    String hash = passwordEncoder.encode(user.getPassword());
    user.setPassword(hash);
    user.setActivated(Boolean.TRUE); // TODO: send sms or email with code for activation
    user.setAuthorities(CollectionsHelper.setOf(Authorities.ROLE_USER));

    // TODO: other routines on account creation

    userRepository.save(user);

    return user;
  }

  private void throwIfUsernameExists(String username) {
    Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(username));
    existingUser.ifPresent(
        (user) -> {
          throw new IllegalArgumentException("user already exists");
        });
  }
}
