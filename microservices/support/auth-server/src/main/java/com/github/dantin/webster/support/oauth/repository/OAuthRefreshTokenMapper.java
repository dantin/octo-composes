package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthRefreshToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OAuthRefreshTokenMapper {

  OAuthRefreshToken findOneByTokenId(String tokenId);

  void save(OAuthRefreshToken refreshToken);

  void deleteByTokenId(String tokenId);
}
