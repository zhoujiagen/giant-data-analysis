package com.spike.giantdataanalysis.task.store.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = "com.spike.giantdataanalysis.task.store.domain")
@EnableJpaRepositories(basePackages = "com.spike.giantdataanalysis.task.store.repository")
@EnableTransactionManagement
public class HibernateJpaConfig {

}
