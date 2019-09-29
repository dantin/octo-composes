package com.github.dantin.webster.support.oauth.config;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthRefreshToken;
import com.github.dantin.webster.support.oauth.service.OAuthTokenService;
import com.github.dantin.webster.support.oauth.util.SerializableObjectConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class OAuthTokenStore implements TokenStore {

  private final AuthenticationKeyGenerator authenticationKeyGenerator;

  private final OAuthTokenService authTokenService;

  public OAuthTokenStore(OAuthTokenService authTokenService) {
    this.authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    this.authTokenService = authTokenService;
  }

  @Override
  public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
    return readAuthentication(accessToken.getValue());
  }

  @Override
  public OAuth2Authentication readAuthentication(String token) {
    String tokenId = authTokenService.extractTokenKey(token);

    Optional<OAuthAccessToken> accessToken = authTokenService.findAccessToken(tokenId);
    return accessToken.map(OAuthAccessToken::getAuthentication).orElse(null);
  }

  @Override
  public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    String refreshToken = null;
    if (!Objects.isNull(accessToken.getRefreshToken())) {
      refreshToken = accessToken.getRefreshToken().getValue();
    }

    if (!Objects.isNull(accessToken.getValue())) {
      // remove old access token.
      removeAccessToken(accessToken);
    }

    OAuthAccessToken token =
        OAuthAccessToken.builder(authTokenService.extractTokenKey(accessToken.getValue()))
            .token(accessToken)
            .authenticationId(authenticationKeyGenerator.extractKey(authentication))
            .username(authentication.isClientOnly() ? null : authentication.getName())
            .clientId(authentication.getOAuth2Request().getClientId())
            .authentication(SerializableObjectConverter.serialize(authentication))
            .refreshToken(authTokenService.extractTokenKey(refreshToken))
            .build();
    authTokenService.saveAccessToken(token);
  }

  @Override
  public OAuth2AccessToken readAccessToken(String tokenValue) {
    String tokenId = authTokenService.extractTokenKey(tokenValue);
    Optional<OAuthAccessToken> accessToken = authTokenService.findAccessToken(tokenId);
    return accessToken.map(OAuthAccessToken::getToken).orElse(null);
  }

  @Override
  public void removeAccessToken(OAuth2AccessToken token) {
    String tokenId = authTokenService.extractTokenKey(token.getValue());
    authTokenService.removeAccessToken(tokenId);
  }

  @Override
  public void storeRefreshToken(
      OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
    OAuthRefreshToken token =
        OAuthRefreshToken.builder(authTokenService.extractTokenKey(refreshToken.getValue()))
            .token(refreshToken)
            .authentication(authentication)
            .build();
    authTokenService.saveRefreshToken(token);
  }

  @Override
  public OAuth2RefreshToken readRefreshToken(String tokenValue) {
    String tokenId = authTokenService.extractTokenKey(tokenValue);
    Optional<OAuthRefreshToken> refreshToken = authTokenService.findRefreshToken(tokenId);
    return refreshToken.map(OAuthRefreshToken::getToken).orElse(null);
  }

  @Override
  public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
    String tokenId = authTokenService.extractTokenKey(token.getValue());
    Optional<OAuthRefreshToken> refreshToken = authTokenService.findRefreshToken(tokenId);
    return refreshToken.map(OAuthRefreshToken::getAuthentication).orElse(null);
  }

  @Override
  public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
    String tokenId = authTokenService.extractTokenKey(refreshToken.getValue());
    authTokenService.removeRefreshToken(tokenId);
  }

  @Override
  public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
    String refreshTokenId = authTokenService.extractTokenKey(refreshToken.getValue());
    authTokenService.removeAccessTokenByRefreshTokenId(refreshTokenId);
  }

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
    OAuth2AccessToken accessToken = null;
    String authenticationId = authenticationKeyGenerator.extractKey(authentication);

    Optional<OAuthAccessToken> existAccessToken =
        authTokenService.findAccessTokenByAuthenticationId(authenticationId);
    if (existAccessToken.isPresent()) {
      accessToken = existAccessToken.get().getToken();
      if (!Objects.isNull(accessToken)
          && !authenticationId.equals(
              authenticationKeyGenerator.extractKey(readAuthentication(accessToken)))) {
        removeAccessToken(accessToken);
        storeAccessToken(accessToken, authentication);
      }
    }
    return accessToken;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(
      String clientId, String userName) {
    Collection<OAuth2AccessToken> tokens = new ArrayList<>();
    authTokenService
        .findAccessTokenByClientIdAndUsername(clientId, userName)
        .forEach(it -> tokens.add(it.getToken()));
    return tokens;
  }

  @Override
  public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
    Collection<OAuth2AccessToken> tokens = new ArrayList<>();
    authTokenService.findAccessTokenByClientId(clientId).forEach(it -> tokens.add(it.getToken()));
    return tokens;
  }
}
