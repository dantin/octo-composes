package com.github.dantin.webster.support.oauth.entity.domain;

import com.github.dantin.webster.common.base.CollectionsHelper;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

public class AuthClientDetails implements ClientDetails {

  private String id;

  private String clientId;

  private String clientSecret;

  private String grantTypes;

  private String scopes;

  private String resources;

  private String redirectUris;

  private Integer accessTokenValidity;

  private Integer refreshTokenValidity;

  private String additionalInformation;

  @Override
  public String getClientId() {
    return clientId;
  }

  @Override
  public Set<String> getResourceIds() {
    return !Objects.isNull(resources) ? CollectionsHelper.setOf(resources.split(",")) : null;
  }

  @Override
  public boolean isSecretRequired() {
    return true;
  }

  @Override
  public String getClientSecret() {
    return clientSecret;
  }

  @Override
  public boolean isScoped() {
    return false;
  }

  @Override
  public Set<String> getScope() {
    return !Objects.isNull(scopes) ? CollectionsHelper.setOf(scopes.split(",")) : null;
  }

  @Override
  public Set<String> getAuthorizedGrantTypes() {
    return !Objects.isNull(grantTypes) ? CollectionsHelper.setOf(grantTypes.split(",")) : null;
  }

  @Override
  public Set<String> getRegisteredRedirectUri() {
    return !Objects.isNull(redirectUris) ? CollectionsHelper.setOf(redirectUris.split(",")) : null;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return CollectionsHelper.listOf();
  }

  @Override
  public Integer getAccessTokenValiditySeconds() {
    return accessTokenValidity;
  }

  @Override
  public Integer getRefreshTokenValiditySeconds() {
    return refreshTokenValidity;
  }

  @Override
  public boolean isAutoApprove(String scope) {
    return true;
  }

  @Override
  public Map<String, Object> getAdditionalInformation() {
    return null;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public void setGrantTypes(String grantTypes) {
    this.grantTypes = grantTypes;
  }

  public void setScopes(String scopes) {
    this.scopes = scopes;
  }

  public void setResources(String resources) {
    this.resources = resources;
  }

  public void setRedirectUris(String redirectUris) {
    this.redirectUris = redirectUris;
  }

  public void setAccessTokenValidity(Integer accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
  }

  public void setRefreshTokenValidity(Integer refreshTokenValidity) {
    this.refreshTokenValidity = refreshTokenValidity;
  }

  public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
  }
}
