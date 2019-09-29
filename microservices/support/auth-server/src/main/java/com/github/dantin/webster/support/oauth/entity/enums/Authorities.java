package com.github.dantin.webster.support.oauth.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authorities implements GrantedAuthority {
  ROLE_ADMIN,
  ROLE_ROOT,
  ROLE_USER;

  @Override
  public String getAuthority() {
    return name();
  }
}
