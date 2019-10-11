package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.*;

import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthRefreshToken;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

public class OAuthRefreshTokenMapperTest extends BaseSpringBootTest {

  @Autowired OAuthRefreshTokenMapper repository;

  private final String expectedTokenId = "test-token-01";
  private final String expectedClientId = "test-client-01";
  private final String expectedRefreshTokenValue = "test-refresh-token";

  private OAuthRefreshToken refreshToken;

  @Before
  public void setUp() {
    OAuth2Request request =
        new OAuth2Request(
            Collections.emptyMap(), expectedClientId, null, false, null, null, null, null, null);
    OAuth2Authentication authentication = new OAuth2Authentication(request, null);
    refreshToken =
        OAuthRefreshToken.builder(expectedTokenId)
            .authentication(authentication)
            .token(new DefaultOAuth2RefreshToken(expectedRefreshTokenValue))
            .build();
  }

  @Test
  public void testBaseOperation() {
    OAuthRefreshToken found = repository.findOneByTokenId(expectedTokenId);
    assertNull("target access token already exists", found);

    repository.save(refreshToken);

    found = repository.findOneByTokenId(expectedTokenId);
    assertNotNull("create failed", found);

    assertEquals("find by token id failed", expectedTokenId, found.getTokenId());

    repository.deleteByTokenId(expectedTokenId);
    found = repository.findOneByTokenId(expectedTokenId);
    assertNull("delete by token id failed", found);
  }
}
