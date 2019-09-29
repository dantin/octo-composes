package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.AuthClientDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthClientRepository {

  AuthClientDetails findByClientId(String clientId);
}
