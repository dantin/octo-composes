package com.githut.dantin.webster.support.configuration;

import com.github.dantin.webster.support.configuration.ConfigServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ConfigServerApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigServerApplicationTests {

  @Test
  public void contextLoads() {}
}
