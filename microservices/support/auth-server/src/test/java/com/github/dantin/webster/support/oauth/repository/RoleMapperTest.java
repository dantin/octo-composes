package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.*;

import com.github.dantin.webster.common.base.CollectionsHelper;
import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import com.github.dantin.webster.support.oauth.entity.domain.Role;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleMapperTest extends BaseSpringBootTest {

  @Autowired private RoleMapper repository;

  private final String expectedId = "test-01";
  private final String expectedName = "test-role";

  private Role role;

  @Before
  public void setUp() {
    role = Role.builder(expectedId, expectedName).build();
  }

  @Test
  public void testBasicOperation() {
    Role found = repository.findOneByName(expectedName);
    assertNull("target role already exist", found);

    repository.save(role);

    found = repository.findOneByName(expectedName);
    assertNotNull("create failed", found);

    assertEquals("find by name failed", expectedId, found.getId());

    List<Role> roles = repository.findAllByNames(CollectionsHelper.listOf());
    assertEquals(1, roles.size());
    roles = repository.findAllByNames(CollectionsHelper.listOf("not-exist"));
    assertEquals(0, roles.size());
    roles = repository.findAllByNames(CollectionsHelper.listOf(expectedName));
    assertEquals(1, roles.size());
    assertEquals(expectedName, roles.get(0).getName());

    repository.deleteById(expectedId);
    found = repository.findOneByName(expectedName);
    assertNull("delete by id failed", found);
  }
}
