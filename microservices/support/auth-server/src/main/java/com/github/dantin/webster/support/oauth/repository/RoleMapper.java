package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.Role;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface RoleMapper {

  void save(Role role);

  Role findByName(String name);

  List<Role> findAll();

  List<Role> findAllByName(List<String> names);

  void deleteById(String id);
}
