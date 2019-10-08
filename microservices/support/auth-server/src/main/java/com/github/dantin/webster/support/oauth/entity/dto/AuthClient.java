package com.github.dantin.webster.support.oauth.entity.dto;

import com.github.dantin.webster.common.base.CollectionsHelper;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthClientDetails;
import com.google.common.base.Strings;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

public class AuthClient implements ClientDetails {

  private static final String COMMA = ",";

  private final OAuthClientDetails clientDetails;

  private AuthClient(Builder builder) {
    this.clientDetails = builder.clientDetails;
  }

  @Override
  public String getClientId() {
    return clientDetails.getClientId();
  }

  @Override
  public Set<String> getResourceIds() {
    if (Strings.isNullOrEmpty(clientDetails.getResourceIds())) {
      return CollectionsHelper.setOf();
    }
    return CollectionsHelper.setOf(clientDetails.getResourceIds().split(COMMA));
  }

  @Override
  public boolean isSecretRequired() {
    return true;
  }

  @Override
  public String getClientSecret() {
    return clientDetails.getClientSecret();
  }

  @Override
  public boolean isScoped() {
    return false;
  }

  @Override
  public Set<String> getScope() {
    if (Strings.isNullOrEmpty(clientDetails.getScope())) {
      return CollectionsHelper.setOf();
    }
    return CollectionsHelper.setOf(clientDetails.getScope().split(COMMA));
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    if (Strings.isNullOrEmpty(clientDetails.getAuthorizedGrantTypes())) {
      return CollectionsHelper.setOf();
    }
    return CollectionsHelper.setOf(clientDetails.getAuthorizedGrantTypes().split(COMMA));
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    if (Strings.isNullOrEmpty(clientDetails.getWebServerRedirectUri())) {
      return CollectionsHelper.setOf();
    }
    return CollectionsHelper.setOf(clientDetails.getWebServerRedirectUri().split(COMMA));
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return CollectionsHelper.listOf();
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return clientDetails.getAccessTokenValidity();
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return clientDetails.getRefreshTokenValidity();
  }

  @Override
  public boolean isAutoApprove(String scope) {
    // TODO: auto approve by scope
    return true;
  }

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return CollectionsHelper.mapOf();
  }

  public static Builder builder(OAuthClientDetails clientDetails) {
    return new Builder(clientDetails);
  }

  public static class Builder implements com.github.dantin.webster.common.base.Builder<AuthClient> {
    private final OAuthClientDetails clientDetails;

    private Builder(OAuthClientDetails clientDetails) {
      if (Objects.isNull(clientDetails)) {
        throw new IllegalStateException("client details is not set");
      }
      this.clientDetails = clientDetails;
    }

    @Override
    public AuthClient build() {
      return new AuthClient(this);
    }
  }
}
