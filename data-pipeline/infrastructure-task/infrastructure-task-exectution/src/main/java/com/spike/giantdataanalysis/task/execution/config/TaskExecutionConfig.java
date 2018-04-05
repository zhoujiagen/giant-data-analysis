package com.spike.giantdataanalysis.task.execution.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// @PropertySource({ "classpath:task.properties" })
@Configuration
@EnableConfigurationProperties({ TaskExecutionProperties.class })
@ComponentScan({ "com.spike.giantdataanalysis.task.execution" })
public class TaskExecutionConfig {

}
