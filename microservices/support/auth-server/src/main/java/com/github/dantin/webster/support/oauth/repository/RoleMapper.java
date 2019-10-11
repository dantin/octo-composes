package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.Role;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface RoleMapper {

  void save(Role role);

  Role findOneByName(String name);

  List<Role> findAllByNames(@Param("names") List<String> names);

  void deleteById(String id);
}
