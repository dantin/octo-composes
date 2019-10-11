package com.github.dantin.webster.support.oauth.entity.domain;

import java.util.*;
import org.joda.time.DateTime;

public class User {

  private String id;

  private String username;

  private String password;

  private Set<Role> authorities;

  private Date createAt;

  private Date updateAt;

  public User() {}

  private User(Builder builder) {
    this.id = builder.id;
    this.username = builder.username;
    this.password = builder.password;
    this.authorities = builder.roles;
    this.createAt = builder.createAt;
    this.updateAt = builder.updateAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  public Date getUpdateAt() {
    return updateAt;
  }

  public void setUpdateAt(Date updateAt) {
    this.updateAt = updateAt;
  }

  public static Builder builder(String id, String username, String password) {
    return new Builder(id, username, password);
  }

  public static final class Builder implements com.github.dantin.webster.common.base.Builder<User> {
    private final String id;
    private final String username;
    private final String password;
    private final Set<Role> roles;
    private Date createAt;
    private Date updateAt;

    private Builder(String id, String username, String password) {
      this.id = id;
      this.username = username;
      this.password = password;
      this.roles = new HashSet<>();
    }

    public Builder addRole(Role role) {
      if (!Objects.isNull(role)) {
        roles.add(role);
      }
      return this;
    }

    @Override
    public User build() {
      Date now = DateTime.now().toDate();
      this.createAt = now;
      this.updateAt = now;
      return new User(this);
    }
  }
}
