package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

  User findByUsername(String username);

  void save(User user);
}
