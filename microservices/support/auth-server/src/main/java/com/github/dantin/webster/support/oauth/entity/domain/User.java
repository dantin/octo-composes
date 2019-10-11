package com.github.dantin.webster.support.oauth.entity.domain;

import java.util.Date;
import java.util.Set;

public class User {

  private String id;

  private String username;

  private String password;

  private Set<Role> authorities;

  private Date createAt;

  private Date updateAt;

  public User() {}

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

  public Set<Role> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Role> authorities) {
    this.authorities = authorities;
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
}
