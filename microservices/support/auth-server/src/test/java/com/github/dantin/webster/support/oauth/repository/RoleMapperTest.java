package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.*;

import com.github.dantin.webster.common.base.CollectionsHelper;
import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import com.github.dantin.webster.support.oauth.entity.domain.Role;
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
    assertNull("target role already exists", repository.findOneByName(expectedName));

    repository.save(role);

    Role found = repository.findOneByName(expectedName);
    assertNotNull("create failed", found);

    assertEquals("wrong value", expectedId, found.getId());
    assertEquals("wrong value", expectedName, found.getName());

    assertEquals(1, repository.findAllByNames(CollectionsHelper.listOf()).size());
    assertEquals(0, repository.findAllByNames(CollectionsHelper.listOf("not-exist")).size());
    assertEquals(1, repository.findAllByNames(CollectionsHelper.listOf(expectedName)).size());

    assertEquals(1, repository.findAllByIds(CollectionsHelper.listOf()).size());
    assertEquals(0, repository.findAllByIds(CollectionsHelper.listOf("not-exist")).size());
    assertEquals(1, repository.findAllByIds(CollectionsHelper.listOf(expectedId)).size());

    repository.deleteById(expectedId);
    assertNull("delete by id failed", repository.findOneByName(expectedName));
  }
}
