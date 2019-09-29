package com.github.dantin.webster.support.oauth.service.impl;

import com.github.dantin.webster.support.oauth.repository.AuthClientRepository;
import java.util.Optional;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class AuthClientDetailsService implements ClientDetailsService {

  private final AuthClientRepository authClientRepository;

  public AuthClientDetailsService(AuthClientRepository authClientRepository) {
    this.authClientRepository = authClientRepository;
  }

  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    Optional<ClientDetails> existClientDetails =
        Optional.ofNullable(authClientRepository.findByClientId(clientId));
    return existClientDetails.orElseThrow(IllegalAccessError::new);
  }
}
