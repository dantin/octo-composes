package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OAuthAccessTokenRepository {

  OAuthAccessToken findByTokenId(@Param("tokenId") String tokenId);

  OAuthAccessToken findByAuthenticationId(@Param("authenticationId") String authenticationId);

  List<OAuthAccessToken> findByClientId(String clientId);

  List<OAuthAccessToken> findByClientIdAndUsername(String clientId, String username);

  void save(OAuthAccessToken accessToken);

  void deleteByTokenId(String tokenId);

  void deleteByRefreshTokenId(String tokenId);
}