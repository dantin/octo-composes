package com.github.dantin.webster.support.oauth.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.SerializationUtils;

public class SerializableObjectConverter {

  private SerializableObjectConverter() {
    throw new AssertionError();
  }

  public static String serialize(OAuth2Authentication rawValue) {
    byte[] bytes = SerializationUtils.serialize(rawValue);
    return Base64.encodeBase64String(bytes);
  }

  public static OAuth2Authentication deserialize(String encodedValue) {
    byte[] bytes = Base64.decodeBase64(encodedValue);
    return (OAuth2Authentication) SerializationUtils.deserialize(bytes);
  }
}
