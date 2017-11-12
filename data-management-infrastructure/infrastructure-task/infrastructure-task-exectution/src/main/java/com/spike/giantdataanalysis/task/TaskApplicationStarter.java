package com.spike.giantdataanalysis.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 任务执行启动入口.
 * @author zhoujiagen
 */
@SpringBootApplication
public class TaskApplicationStarter {

  public static void main(String[] args) {
    SpringApplication.run(TaskApplicationStarter.class, args);
  }
}
