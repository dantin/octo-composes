package com.github.dantin.webster.support.oauth.config;

import com.github.dantin.webster.support.oauth.service.OAuthTokenService;
import com.github.dantin.webster.support.oauth.service.impl.AuthClientDetailsService;
import com.github.dantin.webster.support.oauth.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

  private final AuthenticationManager authenticationManager;

  private final CustomUserDetailsService userDetailsService;

  private final AuthClientDetailsService authClientDetailsService;

  private final OAuthTokenService authTokenService;

  private final PasswordEncoder encoder;

  public OAuth2AuthorizationConfig(
      @Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
      CustomUserDetailsService userDetailsService,
      AuthClientDetailsService authClientDetailsService,
      OAuthTokenService authTokenService,
      PasswordEncoder encoder) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.authClientDetailsService = authClientDetailsService;
    this.authTokenService = authTokenService;
    this.encoder = encoder;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(authClientDetailsService);
  }

  @Bean
  public TokenStore tokenStore() {
    return new OAuthTokenStore(authTokenService);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .tokenStore(tokenStore())
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    oauthServer
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()")
        .passwordEncoder(encoder)
        .allowFormAuthenticationForClients();
  }
}
