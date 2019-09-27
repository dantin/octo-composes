package com.github.dantin.webster.support.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:/application.properties")
@SpringBootTest(classes = TestOnlyApplication.class, webEnvironment = WebEnvironment.MOCK)
public class GatewayServerApplicationTests {

  @Test
  public void contextLoads() {}
}
