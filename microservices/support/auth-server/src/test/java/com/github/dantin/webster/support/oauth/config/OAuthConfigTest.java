package com.github.dantin.webster.support.oauth.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.github.dantin.webster.support.oauth.BaseSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class OAuthConfigTest extends BaseSpringBootTest {

  private static final String ENCODING_ID = "bcrypt";

  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  public void testPasswordEncoder() {
    String rawPassword = "change1t";
    assertNotNull(passwordEncoder);
    String encodedPassword = passwordEncoder.encode(rawPassword);
    System.out.println(encodedPassword);
    assertEquals(
        "encoded password wrong",
        String.format("{%s}", ENCODING_ID),
        encodedPassword.substring(0, 2 + ENCODING_ID.length()));
  }
}
