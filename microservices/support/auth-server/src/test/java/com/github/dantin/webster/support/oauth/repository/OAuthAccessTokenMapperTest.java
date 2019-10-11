package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.*;

import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

public class OAuthAccessTokenMapperTest extends BaseSpringBootTest {

  @Autowired OAuthAccessTokenMapper repository;

  private final String expectedTokenId = "test-token-01";
  private final String expectedClientId = "test-client-01";
  private final String expectedAuthenticationId = "test-authentication-01";
  private final String expectedUsername = "username";
  private final String expectedTokenValue = "test-token";
  private final String expectedRefreshTokenValue = "test-refresh-token";

  private OAuthAccessToken accessToken;

  @Before
  public void setUp() {
    OAuth2Request request =
        new OAuth2Request(
            Collections.emptyMap(), expectedClientId, null, false, null, null, null, null, null);
    OAuth2Authentication authentication = new OAuth2Authentication(request, null);
    accessToken =
        OAuthAccessToken.builder(expectedTokenId)
            .refreshToken(expectedRefreshTokenValue)
            .authentication(authentication)
            .clientId(expectedClientId)
            .username(expectedUsername)
            .authenticationId(expectedAuthenticationId)
            .token(new DefaultOAuth2AccessToken(expectedTokenValue))
            .build();
  }

  @Test
  public void testBasicOperation() {
    assertNull("target access token already exists", repository.findOneByTokenId(expectedTokenId));
    repository.save(accessToken);

    OAuthAccessToken found = repository.findOneByTokenId(expectedTokenId);

    assertNotNull("create failed", found);
    assertEquals("wrong value", expectedTokenId, found.getTokenId());
    assertEquals("wrong value", expectedClientId, found.getClientId());

    repository.deleteByTokenId(expectedTokenId);
    assertNull("delete by token id failed", repository.findOneByTokenId(expectedTokenId));
  }

  @Test
  public void testDeleteByRefreshToken() {
    assertNull("target access token already exists", repository.findOneByTokenId(expectedTokenId));
    repository.save(accessToken);

    assertNotNull("create failed", repository.findOneByTokenId(expectedTokenId));

    repository.deleteByRefreshToken(expectedRefreshTokenValue);
    assertNull("delete by refresh token failed", repository.findOneByTokenId(expectedTokenId));
  }

  @Test
  public void testFindOperations() {
    assertNull("target access token already exists", repository.findOneByTokenId(expectedTokenId));
    repository.save(accessToken);
    assertNotNull("create failed", repository.findOneByTokenId(expectedTokenId));

    // find by authentication id.
    assertNotNull(
        "find by authentication id failed",
        repository.findOneByAuthenticationId(expectedAuthenticationId));
    // find by client id.
    assertEquals(1, repository.findAllByClientId(expectedClientId).size());
    // find by client id and username.
    assertEquals(
        1, repository.findAllByClientIdAndUsername(expectedClientId, expectedUsername).size());
    assertEquals(0, repository.findAllByClientIdAndUsername(expectedClientId, "x").size());

    repository.deleteByTokenId(expectedTokenId);
    assertNull("delete by token id failed", repository.findOneByTokenId(expectedTokenId));
  }
}
