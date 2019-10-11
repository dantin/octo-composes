package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.UserRole;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserRoleMapper {

  void save(UserRole userRole);

  List<UserRole> findAllByUserId(String id);

  void deleteByUserId(String id);
}
