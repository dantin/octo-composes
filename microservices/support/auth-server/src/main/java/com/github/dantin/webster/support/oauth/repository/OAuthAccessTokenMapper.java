package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OAuthAccessTokenMapper {

  OAuthAccessToken findOneByTokenId(String tokenId);

  OAuthAccessToken findOneByAuthenticationId(String authenticationId);

  List<OAuthAccessToken> findAllByClientId(String clientId);

  List<OAuthAccessToken> findAllByClientIdAndUsername(String clientId, String username);

  void save(OAuthAccessToken accessToken);

  void deleteByTokenId(String tokenId);

  void deleteByRefreshToken(String refreshToken);
}
