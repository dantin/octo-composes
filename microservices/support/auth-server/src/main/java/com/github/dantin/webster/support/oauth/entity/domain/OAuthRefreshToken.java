package com.github.dantin.webster.support.oauth.entity.domain;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/** Entity that represent <code>oauth_refresh_token</code>. */
public class OAuthRefreshToken {

  /* encrypted refresh_token */
  private String tokenId;
  /* serialized binary token */
  private OAuth2RefreshToken token;
  /* encrypted binary authentication */
  private OAuth2Authentication authentication;

  public OAuthRefreshToken() {}

  private OAuthRefreshToken(Builder builder) {
    this.tokenId = builder.tokenId;
    this.token = builder.token;
    this.authentication = builder.authentication;
  }

  public String getTokenId() {
    return tokenId;
  }

  public void setTokenId(String tokenId) {
    this.tokenId = tokenId;
  }

  public OAuth2RefreshToken getToken() {
    return token;
  }

  public void setToken(OAuth2RefreshToken token) {
    this.token = token;
  }

  public OAuth2Authentication getAuthentication() {
    return authentication;
  }

  public void setAuthentication(OAuth2Authentication authentication) {
    this.authentication = authentication;
  }

  public static Builder builder(String tokenId) {
    return new Builder(tokenId);
  }

  public static class Builder
      implements com.github.dantin.webster.common.base.Builder<OAuthRefreshToken> {
    private final String tokenId;
    private OAuth2RefreshToken token;
    private OAuth2Authentication authentication;

    private Builder(String tokenId) {
      this.tokenId = tokenId;
    }

    public Builder token(OAuth2RefreshToken token) {
      this.token = token;
      return this;
    }

    public Builder authentication(OAuth2Authentication authentication) {
      this.authentication = authentication;
      return this;
    }

    @Override
    public OAuthRefreshToken build() {
      return new OAuthRefreshToken(this);
    }
  }
}
