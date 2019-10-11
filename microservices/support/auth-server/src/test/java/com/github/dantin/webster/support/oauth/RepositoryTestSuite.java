package com.github.dantin.webster.support.oauth;

import com.github.dantin.webster.support.oauth.config.OAuthConfigTest;
import com.github.dantin.webster.support.oauth.repository.OAuthAccessTokenMapperTest;
import com.github.dantin.webster.support.oauth.repository.OAuthClientMapperTest;
import com.github.dantin.webster.support.oauth.repository.OAuthRefreshTokenMapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  OAuthConfigTest.class,
  OAuthAccessTokenMapperTest.class,
  OAuthRefreshTokenMapperTest.class,
  OAuthClientMapperTest.class
})
public class RepositoryTestSuite {}
