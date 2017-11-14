package com.spike.giantdataanalysis.task.execution.core.context;

import java.util.Set;

import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/** 任务执行上下文的默认实现 */
public final class DefaultTaskExecutionContext implements TaskExecutionContext {

  private TaskStoreService taskStoreService;
  private TaskExecutionProperties config;
  private Set<String> workers;
  private String myWorkId;

  @Override
  public TaskStoreService taskStoreService() {
    return taskStoreService;
  }

  @Override
  public TaskExecutionProperties config() {
    return config;
  }

  @Override
  public Set<String> workers() {
    return workers;
  }

  public void setTaskStoreService(TaskStoreService taskStoreService) {
    this.taskStoreService = taskStoreService;
  }

  public void setConfig(TaskExecutionProperties config) {
    this.config = config;
  }

  public void setWorkers(Set<String> workers) {
    this.workers = workers;
  }

  @Override
  public String myWorkId() {
    return myWorkId;
  }

  public void setMyWorkId(String myWorkId) {
    this.myWorkId = myWorkId;
  }

}