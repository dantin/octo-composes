package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

  User findByUsername(String username);

  void save(User user);
}
