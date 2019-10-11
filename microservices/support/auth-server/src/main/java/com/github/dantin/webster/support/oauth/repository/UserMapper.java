package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.Role;
import com.github.dantin.webster.support.oauth.entity.domain.User;
import com.github.dantin.webster.support.oauth.entity.domain.UserRole;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

  User findByUsername(String username);

  void save(User user);

  void addUserRole(String userId, UserRole role);

  void removeUserRole(String userId, Role role);

  List<UserRole> findAllUserRoleById(String id);
}
