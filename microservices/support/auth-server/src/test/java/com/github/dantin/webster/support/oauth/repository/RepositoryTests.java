package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.assertTrue;

import com.github.dantin.webster.support.oauth.TestOnlyApplication;
import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(
    classes = TestOnlyApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class RepositoryTests {

  @Autowired OAuthAccessTokenRepository accessTokenRepository;

  @Test
  public void shouldInsertAccessToken() {
    assertTrue(!Objects.isNull(accessTokenRepository));
  }
}
