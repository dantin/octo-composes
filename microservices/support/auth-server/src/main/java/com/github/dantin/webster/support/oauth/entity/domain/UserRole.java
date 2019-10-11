package com.github.dantin.webster.support.oauth.entity.domain;

public class UserRole {

  private String id;

  private String userId;

  private String roleId;

  public UserRole() {}

  private UserRole(Builder builder) {
    this.id = builder.id;
    this.userId = builder.userId;
    this.roleId = builder.roleId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public static Builder builder(String id, User user, Role role) {
    return new Builder(id, user.getId(), role.getId());
  }

  public static final class Builder
      implements com.github.dantin.webster.common.base.Builder<UserRole> {
    private final String id;
    private final String userId;
    private final String roleId;

    private Builder(String id, String userId, String roleId) {
      this.id = id;
      this.userId = userId;
      this.roleId = roleId;
    }

    @Override
    public UserRole build() {
      return new UserRole(this);
    }
  }
}
