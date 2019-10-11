package com.github.dantin.webster.support.oauth.controller;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

  @GetMapping("/current")
  public Principal getUser(Principal principal) {
    LOGGER.info("get user info {}", principal.toString());
    return principal;
  }
}
