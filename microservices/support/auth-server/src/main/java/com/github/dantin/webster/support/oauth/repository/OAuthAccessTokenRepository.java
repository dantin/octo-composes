package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthAccessTokenRepository {

  OAuthAccessToken findByTokenId(String tokenId);

  OAuthAccessToken findByAuthenticationId(String authenticationId);

  List<OAuthAccessToken> findByClientId(String clientId);

  List<OAuthAccessToken> findByClientIdAndUsername(String clientId, String username);

  void save(OAuthAccessToken accessToken);

  void deleteByTokenId(String tokenId);

  void deleteByRefreshTokenId(String tokenId);
}
