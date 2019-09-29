package com.github.dantin.webster.support.oauth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(
    classes = TestOnlyApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthServerApplicationTests {

  @Test
  public void contextLoads() {}
}
