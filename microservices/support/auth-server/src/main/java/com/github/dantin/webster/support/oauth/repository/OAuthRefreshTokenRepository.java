package com.github.dantin.webster.support.oauth.repository;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthRefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthRefreshTokenRepository {

  OAuthRefreshToken findByTokenId(String tokenId);

  void save(OAuthRefreshToken refreshToken);

  void deleteByTokenId(String tokenId);
}
