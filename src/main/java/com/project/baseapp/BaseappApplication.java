package com.project.baseapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@EnableCaching
@SpringBootApplication
@ComponentScan({"com.project.baseapp.controller", "com.project.baseapp.queue", "com.project.baseapp.repository", "com.project.baseapp.dao"})
public class BaseappApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(BaseappApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(BaseappApplication.class);
  }

  @Bean
  public ClassLoaderTemplateResolver yourTemplateResolver() {
    ClassLoaderTemplateResolver yourTemplateResolver = new ClassLoaderTemplateResolver();
    yourTemplateResolver.setPrefix("static/");
    yourTemplateResolver.setSuffix(".html");
    yourTemplateResolver.setTemplateMode(TemplateMode.HTML);
    yourTemplateResolver.setCharacterEncoding("UTF-8");
    yourTemplateResolver.setOrder(0);  // this is iportant. This way spring
    //boot will listen to both places 0
    //and 1
//    emailTemplateResolver.setCheckExistence(true);

    return yourTemplateResolver;
  }
}
