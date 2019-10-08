package com.github.dantin.webster.support.oauth.service.impl;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthClientDetails;
import com.github.dantin.webster.support.oauth.entity.dto.AuthClient;
import com.github.dantin.webster.support.oauth.repository.OAuthClientMapper;
import java.util.Optional;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class AuthClientDetailsService implements ClientDetailsService {

  private final OAuthClientMapper OAuthClientMapper;

  public AuthClientDetailsService(OAuthClientMapper OAuthClientMapper) {
    this.OAuthClientMapper = OAuthClientMapper;
  }

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    Optional<OAuthClientDetails> existClientDetails =
        Optional.ofNullable(OAuthClientMapper.findOneByClientId(clientId));
    if (!existClientDetails.isPresent()) {
      throw new IllegalStateException("no client detail found");
    }
    return AuthClient.builder(existClientDetails.get()).build();
  }
}
