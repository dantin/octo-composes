package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.*;

import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    OAuthAccessToken found = repository.findOneByTokenId(expectedTokenId);
    assertNull("target access token already exists", found);

    repository.save(accessToken);

    found = repository.findOneByTokenId(expectedTokenId);
    assertNotNull("create failed", found);

    assertEquals("find by token id failed", expectedTokenId, found.getTokenId());
    assertEquals("find by token id failed", expectedClientId, found.getClientId());

    repository.deleteByTokenId(expectedTokenId);
    found = repository.findOneByTokenId(expectedTokenId);
    assertNull("delete by token id failed", found);
  }

  @Test
  public void testDeleteByRefreshToken() {
    OAuthAccessToken found = repository.findOneByTokenId(expectedTokenId);
    assertTrue("target access token already exists", Objects.isNull(found));
    repository.save(accessToken);
    found = repository.findOneByTokenId(expectedTokenId);
    assertFalse("create failed", Objects.isNull(found));

    repository.deleteByRefreshToken(expectedRefreshTokenValue);
    found = repository.findOneByTokenId(expectedTokenId);
    assertTrue("delete by refresh token failed", Objects.isNull(found));
  }

  @Test
  public void testFindOperations() {
    OAuthAccessToken found = repository.findOneByTokenId(expectedTokenId);
    assertTrue("target access token already exists", Objects.isNull(found));
    repository.save(accessToken);
    found = repository.findOneByTokenId(expectedTokenId);
    assertFalse("create failed", Objects.isNull(found));

    // find by authentication id.
    found = repository.findOneByAuthenticationId(expectedAuthenticationId);
    assertFalse("find by authentication id failed", Objects.isNull(found));

    // find by client id.
    List<OAuthAccessToken> tokens = repository.findAllByClientId(expectedClientId);
    assertEquals(1, tokens.size());

    // find by client id and username.
    tokens = repository.findAllByClientIdAndUsername(expectedClientId, expectedUsername);
    assertEquals(1, tokens.size());
    tokens = repository.findAllByClientIdAndUsername(expectedClientId, "x");
    assertEquals(0, tokens.size());

    repository.deleteByTokenId(expectedTokenId);
  }
}
