package com.github.dantin.webster.support.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/*
 * see: https://www.stackoverflow.com/questions/44486165/how-to-disable-eureka-and-spring-cloud-config-in-a-webmvctest
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest(classes = TestOnlyApplication.class, webEnvironment = WebEnvironment.MOCK)
public class EurekaApplicationTests {

  @Test
  public void contextLoads() {}
}
