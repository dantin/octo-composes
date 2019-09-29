package com.github.dantin.webster.support.oauth.entity.domain;

import com.github.dantin.webster.support.oauth.util.SerializableObjectConverter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/** Entity that represent <code>oauth_access_token</code>. */
public class OAuthAccessToken {

  /* encrypted access_token */
  private String tokenId;
  /* serialized binary data */
  private OAuth2AccessToken token;
  /* encrypted username, client_id, scope */
  private String authenticationId;
  /* login username */
  private String username;
  /* client id */
  private String clientId;
  /* serialized binary data */
  private String authentication;
  /* encrypted refresh_token */
  private String refreshToken;

  public OAuthAccessToken() {}

  private OAuthAccessToken(Builder builder) {
    this.tokenId = builder.tokenId;
    this.token = builder.token;
    this.authenticationId = builder.authenticationId;
    this.username = builder.username;
    this.clientId = builder.clientId;
    this.authentication = builder.authentication;
    this.refreshToken = builder.refreshToken;
  }

  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public OAuth2AccessToken getToken() {
    return token;
  }

  public void setToken(OAuth2AccessToken token) {
    this.token = token;
  }

  public String getAuthenticationId() {
    return authenticationId;
  }

  public void setAuthenticationId(String authenticationId) {
    this.authenticationId = authenticationId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public OAuth2Authentication getAuthentication() {
    return SerializableObjectConverter.deserialize(authentication);
  }

  public void setAuthentication(OAuth2Authentication authentication) {
    this.authentication = SerializableObjectConverter.serialize(authentication);
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public static Builder builder(String clientId) {
    return new Builder(clientId);
  }

  public static class Builder
      implements com.github.dantin.webster.common.base.Builder<OAuthAccessToken> {
    private final String tokenId;
    private OAuth2AccessToken token;
    private String authenticationId;
    private String username;
    private String clientId;
    private String authentication;
    private String refreshToken;

    private Builder(String tokenId) {
      this.tokenId = tokenId;
    }

    public Builder token(OAuth2AccessToken token) {
      this.token = token;
      return this;
    }

    public Builder authenticationId(String authenticationId) {
      this.authenticationId = authenticationId;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    public Builder authentication(String authentication) {
      this.authentication = authentication;
      return this;
    }

    public Builder refreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
      return this;
    }

    @Override
    public OAuthAccessToken build() {
      return new OAuthAccessToken(this);
    }
  }
}
