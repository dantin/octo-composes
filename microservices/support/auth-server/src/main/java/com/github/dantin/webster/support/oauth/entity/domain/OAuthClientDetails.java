package com.github.dantin.webster.support.oauth.entity.domain;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.util.Objects;

/** Entity that represent <code>oauth_client_details</code>. */
public class OAuthClientDetails {

  /* client_id */
  private String clientId;
  /* resource_ids, multiple resources are separate by comma(',') */
  private String resourceIds;
  /* client secret key */
  private String clientSecret;
  /* privilege scope of the client */
  private String scope;
  /* supported grant_type of the client */
  private String authorizedGrantTypes;
  /* redirect_uri */
  private String webServerRedirectUri;
  /* access token valid duration, in second */
  private Integer accessTokenValidity;
  /* refresh token valid duration, in second */
  private Integer refreshTokenValidity;
  /* reserved field */
  private String additionalInformation;
  /* whether auto_approve */
  private String autoApprove;

  public OAuthClientDetails() {}

  private OAuthClientDetails(Builder builder) {
    this.clientId = builder.clientId;
    this.resourceIds = builder.resourceIds;
    this.clientSecret = builder.clientSecret;
    this.scope = builder.scope;
    this.authorizedGrantTypes = builder.authorizedGrantTypes;
    this.webServerRedirectUri = builder.webServerRedirectUri;
    this.accessTokenValidity = builder.accessTokenValidity;
    this.refreshTokenValidity = builder.refreshTokenValidity;
    this.additionalInformation = builder.additionalInformation;
    this.autoApprove = builder.autoApprove;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getResourceIds() {
    return resourceIds;
  }

  public void setResourceIds(String resourceIds) {
    this.resourceIds = resourceIds;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getAuthorizedGrantTypes() {
    return authorizedGrantTypes;
  }

  public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
    this.authorizedGrantTypes = authorizedGrantTypes;
  }

  public String getWebServerRedirectUri() {
    return webServerRedirectUri;
  }

  public void setWebServerRedirectUri(String webServerRedirectUri) {
    this.webServerRedirectUri = webServerRedirectUri;
  }

  public Integer getAccessTokenValidity() {
    return accessTokenValidity;
  }

  public void setAccessTokenValidity(Integer accessTokenValidity) {
    this.accessTokenValidity = accessTokenValidity;
  }

  public Integer getRefreshTokenValidity() {
    return refreshTokenValidity;
  }

  public void setRefreshTokenValidity(Integer refreshTokenValidity) {
    this.refreshTokenValidity = refreshTokenValidity;
  }

  public String getAdditionalInformation() {
    return additionalInformation;
  }

  public void setAdditionalInformation(String additionalInformation) {
    this.additionalInformation = additionalInformation;
  }

  public String getAutoApprove() {
    return autoApprove;
  }

  public void setAutoApprove(String autoApprove) {
    this.autoApprove = autoApprove;
  }

  public static Builder builder(String clientId) {
    return new Builder(clientId);
  }

  public static final class Builder
      implements com.github.dantin.webster.common.base.Builder<OAuthClientDetails> {
    private static final String COMMA = ",";
    private static final Joiner COMMA_JOINER = Joiner.on(COMMA).skipNulls();

    private final String clientId;
    private String resourceIds;
    private String clientSecret;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoApprove;

    private Builder(String id) {
      this.clientId = id;
    }

    public Builder resourceIds(String... ids) {
      if (!Objects.isNull(ids)) {
        this.resourceIds = COMMA_JOINER.join(ids);
      }
      return this;
    }

    public Builder clientSecret(String clientSecret) {
      this.clientSecret = Strings.nullToEmpty(clientSecret);
      return this;
    }

    public Builder scope(String scope) {
      this.scope = Strings.nullToEmpty(scope);
      return this;
    }

    public Builder authorizedGrantTypes(String... authorizedGrantTypes) {
      if (!Objects.isNull(authorizedGrantTypes)) {
        this.authorizedGrantTypes = COMMA_JOINER.join(authorizedGrantTypes);
      }
      return this;
    }

    public Builder webServerRedirectUri(String webServerRedirectUri) {
      this.webServerRedirectUri = Strings.nullToEmpty(webServerRedirectUri);
      return this;
    }

    public Builder accessTokenValidity(int accessTokenValidity) {
      if (accessTokenValidity > 0) {
        this.accessTokenValidity = accessTokenValidity;
      }
      return this;
    }

    public Builder refreshTokenValidity(int refreshTokenValidity) {
      if (refreshTokenValidity > 0) {
        this.refreshTokenValidity = refreshTokenValidity;
      }
      return this;
    }

    public Builder additionalInformation(String additionalInformation) {
      this.additionalInformation = Strings.nullToEmpty(additionalInformation);
      return this;
    }

    public Builder autoApprove(boolean autoApprove) {
      this.autoApprove = Boolean.toString(autoApprove);
      return this;
    }

    @Override
    public OAuthClientDetails build() {
      return new OAuthClientDetails(this);
    }
  }
}
