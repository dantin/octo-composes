package com.github.dantin.webster.support.oauth.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(DruidConfig.class);

  @Bean
  public ServletRegistrationBean<StatViewServlet> druidServlet() {
    LOGGER.info("init Druid Servlet Configuration");
    StatViewServlet statViewServlet = new StatViewServlet();
    ServletRegistrationBean<StatViewServlet> servletRegistrationBean =
        new ServletRegistrationBean<>(statViewServlet, "/druid/*");
    // IP white list
    servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
    // IP black list
    // servletRegistrationBean.addInitParameter("deny", "");
    servletRegistrationBean.addInitParameter("loginUsername", "admin");
    servletRegistrationBean.addInitParameter("loginPassword", "password");
    servletRegistrationBean.addInitParameter("resetEnable", "false");
    return servletRegistrationBean;
  }

  @Bean
  public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
    LOGGER.info("init Druid Web Stat Filter");
    WebStatFilter webStatFilter = new WebStatFilter();
    FilterRegistrationBean<WebStatFilter> filterFilterRegistrationBean =
        new FilterRegistrationBean<>(webStatFilter);
    filterFilterRegistrationBean.addUrlPatterns("/*");
    filterFilterRegistrationBean.addInitParameter(
        "exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
    return filterFilterRegistrationBean;
  }

  @Deprecated
  // Config Bean via druid-spring-boot-starter
  public DataSource dataSource() {
    LOGGER.info("Init DruidDataSource");
    return DruidDataSourceBuilder.create().build();
  }
}
