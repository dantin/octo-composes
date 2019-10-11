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

  User findOneByUsername(String username);

  void save(User user);

  void grantRole(UserRole userRole);

  void revokeRole(User user, Role role);

  void revokeAllRoleById(String id);

  List<UserRole> findAllUserRoleById(String id);

  void deleteById(String id);
}
