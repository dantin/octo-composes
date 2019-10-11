package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.*;

import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import com.github.dantin.webster.support.oauth.entity.domain.Role;
import com.github.dantin.webster.support.oauth.entity.domain.User;
import com.github.dantin.webster.support.oauth.entity.domain.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapperTest extends BaseSpringBootTest {

  @Autowired private UserMapper userMapper;
  @Autowired private RoleMapper roleMapper;

  private final String expectedId = "user-01";
  private final String expectedUsername = "test-user";
  private final String expectedPassword = "password";

  private final String expectedRoleId1 = "role-01";
  private final String expectedRoleName1 = "test-role1";
  private final String expectedRoleId2 = "role-02";
  private final String expectedRoleName2 = "test-role2";

  private final String expectedUserRoleId1 = "user-role-01";
  private final String expectedUserRoleId2 = "user-role-02";

  private User user;
  private Role role1;
  private Role role2;

  @Before
  public void setUp() {
    user = User.builder(expectedId, expectedUsername, expectedPassword).build();
    role1 = Role.builder(expectedRoleId1, expectedRoleName1).build();
    role2 = Role.builder(expectedRoleId2, expectedRoleName2).build();
  }

  @Test
  public void testBasicOperation() {
    User found = userMapper.findOneByUsername(expectedUsername);
    assertNull("target user already exists", found);

    userMapper.save(user);

    found = userMapper.findOneByUsername(expectedUsername);
    assertNotNull("create failed", found);
    assertEquals("create failed", expectedId, found.getId());

    userMapper.deleteById(expectedId);
    found = userMapper.findOneByUsername(expectedUsername);
    assertNull("delete by id failed", found);
  }

  @Test
  public void testUserRoleOperation() {
    assertNull("target role1 already exists", roleMapper.findOneByName(expectedRoleName1));
    roleMapper.save(role1);
    assertNull("target role1 already exists", roleMapper.findOneByName(expectedRoleName2));
    roleMapper.save(role2);

    assertNull("target user already exists", userMapper.findOneByUsername(expectedUsername));
    userMapper.save(user);

    userMapper.grantRole(UserRole.builder(expectedUserRoleId1, user, role1).build());
    userMapper.grantRole(UserRole.builder(expectedUserRoleId2, user, role2).build());

    userMapper.deleteById(expectedId);
    assertNull("delete by id failed", userMapper.findOneByUsername(expectedUsername));

    roleMapper.deleteById(expectedRoleId2);
    assertNull("delete role by id failed", roleMapper.findOneByName(expectedRoleName2));
    roleMapper.deleteById(expectedRoleId1);
    assertNull("delete role by id failed", roleMapper.findOneByName(expectedRoleName1));
  }
}
