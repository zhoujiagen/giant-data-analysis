package com.spike.giantdataanalysis.task.rest;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

// 暂时屏蔽Session autoconfig
@SpringBootApplication(exclude = { SessionAutoConfiguration.class })
public class InfrastructureTaskApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext ctx =
        SpringApplication.run(InfrastructureTaskApplication.class, args);

    String[] beanNames = ctx.getBeanDefinitionNames();
    Arrays.sort(beanNames);
    System.out.println("ApplicationContext Bean definitions: ");
    for (String beanName : beanNames) {
      System.out.println("|-- " + beanName);
    }
  }
}
