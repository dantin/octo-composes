package com.github.dantin.webster.support.oauth.entity.domain;

public class Role {

  private String id;

  private String name;

  public Role() {}

  private Role(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static Builder builder(String id, String name) {
    return new Builder(id, name);
  }

  public static final class Builder implements com.github.dantin.webster.common.base.Builder<Role> {

    private final String id;
    private final String name;

    private Builder(String id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public Role build() {
      return new Role(this);
    }
  }
}
