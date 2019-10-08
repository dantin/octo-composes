package com.github.dantin.webster.support.oauth.service.impl;

import com.github.dantin.webster.support.oauth.entity.domain.OAuthAccessToken;
import com.github.dantin.webster.support.oauth.entity.domain.OAuthRefreshToken;
import com.github.dantin.webster.support.oauth.repository.OAuthAccessTokenMapper;
import com.github.dantin.webster.support.oauth.repository.OAuthRefreshTokenMapper;
import com.github.dantin.webster.support.oauth.service.OAuthTokenService;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OAuthTokenServiceImpl implements OAuthTokenService {

  private static final Logger LOGGER = LoggerFactory.getLogger(OAuthTokenServiceImpl.class);

  private final OAuthAccessTokenMapper accessTokenMapper;
  private final OAuthRefreshTokenMapper refreshTokenRepository;

  public OAuthTokenServiceImpl(
      OAuthAccessTokenMapper accessTokenMapper, OAuthRefreshTokenMapper refreshTokenRepository) {
    // auto wire repository.
    this.accessTokenMapper = accessTokenMapper;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public Optional<OAuthAccessToken> findAccessToken(String tokenId) {
    return Optional.ofNullable(accessTokenMapper.findOneByTokenId(tokenId));
  }

  @Override
  public Optional<OAuthAccessToken> findAccessTokenByAuthenticationId(String authenticationId) {
    return Optional.ofNullable(accessTokenMapper.findOneByAuthenticationId(authenticationId));
  }

  @Override
  public List<OAuthAccessToken> findAccessTokenByClientId(String clientId) {
    return accessTokenMapper.findAllByClientId(clientId);
  }

  @Override
  public List<OAuthAccessToken> findAccessTokenByClientIdAndUsername(
      String clientId, String username) {
    return accessTokenMapper.findAllByClientIdAndUsername(clientId, username);
  }

  @Override
  public void saveAccessToken(OAuthAccessToken accessToken) {
    accessTokenMapper.save(accessToken);
  }

  @Override
  public void removeAccessToken(String tokenId) {
    accessTokenMapper.deleteByTokenId(tokenId);
  }

  @Override
  public void removeAccessTokenByRefreshToken(String refreshToken) {
    accessTokenMapper.deleteByRefreshToken(refreshToken);
  }

  @Override
  public Optional<OAuthRefreshToken> findRefreshToken(String tokenId) {
    return Optional.ofNullable(refreshTokenRepository.findOneByTokenId(tokenId));
  }

  @Override
  public void saveRefreshToken(OAuthRefreshToken refreshToken) {
    refreshTokenRepository.save(refreshToken);
  }

  @Override
  public void removeRefreshToken(String tokenId) {
    refreshTokenRepository.deleteByTokenId(tokenId);
  }

  @Override
  public String extractTokenKey(String value) {
    if (Objects.isNull(value)) {
      return null;
    }

    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("MD5 algorithm not available, should be in the JDK");
    }

    try {
      byte[] raw = digest.digest(value.getBytes(StandardCharsets.UTF_8.name()));
      return String.format("%032x", new BigInteger(1, raw));
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF-8 encoding not available, should be in the JDK");
    }
  }
}
