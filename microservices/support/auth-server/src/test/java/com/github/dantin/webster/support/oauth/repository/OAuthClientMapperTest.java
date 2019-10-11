package com.github.dantin.webster.support.oauth.repository;

import static org.junit.Assert.*;

import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthClientDetails;
import org.bouncycastle.util.Strings;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class OAuthClientMapperTest extends BaseSpringBootTest {

  @Autowired OAuthClientMapper repository;

  private final String expectedClientId = "test-client-01";
  private final String expectedResourceIds = "client_credentials,refresh_code";
  private final String expectedClientSecret = "test-client-secret";
  private final String expectedScope = "service";
  private final String expectedAuthorizedGrantTypes = "client_credentials,refresh_code";
  private final String expectedWebServerRedirectUri = "http://127.0.0.1:8080/console";
  private final int expectedAccessTokenValidity = 60 * 60 * 24;
  private final int expectedRefreshTokenValidity = 60 * 60 * 1;
  private final String expectedAdditionalInformation = "empty";
  private final boolean expectedAutoApprove = true;

  private OAuthClientDetails clientDetails;

  @Before
  public void setUp() {
    clientDetails =
        OAuthClientDetails.builder(expectedClientId)
            .resourceIds(Strings.split(expectedResourceIds, ','))
            .clientSecret(expectedClientSecret)
            .scope(expectedScope)
            .authorizedGrantTypes(Strings.split(expectedAuthorizedGrantTypes, ','))
            .webServerRedirectUri(expectedWebServerRedirectUri)
            .accessTokenValidity(expectedAccessTokenValidity)
            .refreshTokenValidity(expectedRefreshTokenValidity)
            .additionalInformation(expectedAdditionalInformation)
            .autoApprove(expectedAutoApprove)
            .build();
  }

  @Test
  public void testBaseOperation() {
    OAuthClientDetails found = repository.findOneByClientId(expectedClientId);
    assertNull("target auth client already exists", found);

    repository.save(clientDetails);

    found = repository.findOneByClientId(expectedClientId);
    assertNotNull("create failed", found);

    assertEquals("find by client id failed", expectedClientId, found.getClientId());

    repository.deleteByClientId(expectedClientId);
    found = repository.findOneByClientId(expectedClientId);
    assertNull("delete by client id failed", found);
  }

  @Ignore
  @Test
  public void initClient() {
    String secret = "{bcrypt}" + new BCryptPasswordEncoder().encode("try1now");
    // See: DefaultTokenServices
    OAuthClientDetails client =
        OAuthClientDetails.builder("account-service-client")
            .clientSecret(secret)
            .resourceIds("account")
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scope("service")
            .accessTokenValidity(60 * 60 * 24 * 30) // 30 days
            .refreshTokenValidity(60 * 60 * 12) // 12 hours
            .webServerRedirectUri("")
            .additionalInformation("")
            .autoApprove(true)
            .build();
    repository.save(client);
  }
}
