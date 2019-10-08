package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthClientDetails;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OAuthClientMapper {

  OAuthClientDetails findOneByClientId(String clientId);

  void save(OAuthClientDetails clientDetails);

  void deleteByClientId(String clientId);
}
