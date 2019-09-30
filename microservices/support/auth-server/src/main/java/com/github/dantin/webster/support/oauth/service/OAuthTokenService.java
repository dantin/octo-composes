package com.github.dantin.webster.support.oauth.service;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthRefreshToken;
import java.util.List;
import java.util.Optional;

public interface OAuthTokenService {

  String extractTokenKey(String value);

  Optional<OAuthAccessToken> findAccessToken(String tokenId);

  Optional<OAuthAccessToken> findAccessTokenByAuthenticationId(String authenticationId);

  List<OAuthAccessToken> findAccessTokenByClientId(String clientId);

  List<OAuthAccessToken> findAccessTokenByClientIdAndUsername(String clientId, String username);

  void saveAccessToken(OAuthAccessToken accessToken);

  void removeAccessToken(String tokenId);

  void removeAccessTokenByRefreshToken(String refreshToken);

  Optional<OAuthRefreshToken> findRefreshToken(String tokenId);

  void saveRefreshToken(OAuthRefreshToken refreshToken);

  void removeRefreshToken(String tokenId);
}
