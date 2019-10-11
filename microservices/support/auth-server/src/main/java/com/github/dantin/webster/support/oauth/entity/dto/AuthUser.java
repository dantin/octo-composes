package com.github.dantin.webster.support.oauth.entity.dto;

import com.github.dantin.webster.support.oauth.entity.domain.User;
import com.github.dantin.webster.support.oauth.entity.enums.Authorities;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUser implements UserDetails {

  private String id;

  private String username;

  private String password;

  private boolean activated;

  private String activationKey;

  private String resetPasswordKey;

  private Set<Authorities> authorities = new HashSet<>();

  public AuthUser() {}

  private AuthUser(Builder builder) {
    this.id = builder.user.getId();
    this.username = builder.user.getUsername();
    this.password = builder.user.getPassword();
    this.activated = true;
    this.activationKey = "";
    this.resetPasswordKey = "";
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return activated;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public String getResetPasswordKey() {
    return resetPasswordKey;
  }

  public void setResetPasswordKey(String resetPasswordKey) {
    this.resetPasswordKey = resetPasswordKey;
  }

  @Override
  public List<Authorities> getAuthorities() {
    return new ArrayList<>(authorities);
  }

  public void setAuthorities(Set<Authorities> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthUser user = (AuthUser) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public static Builder builder(User user) {
    return new Builder(user);
  }

  public static final class Builder
      implements com.github.dantin.webster.common.base.Builder<AuthUser> {
    private final User user;

    private Builder(User user) {
      this.user = user;
    }

    @Override
    public AuthUser build() {
      return new AuthUser(this);
    }
  }
}
