package com.github.dantin.webster.support.oauth;

import com.github.dantin.webster.support.oauth.config.OAuthConfigTest;
import com.github.dantin.webster.support.oauth.repository.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  OAuthConfigTest.class,
  OAuthAccessTokenMapperTest.class,
  OAuthRefreshTokenMapperTest.class,
  OAuthClientMapperTest.class,
  RoleMapperTest.class,
  UserMapperTest.class,
})
public class RepositoryTestSuite {}
