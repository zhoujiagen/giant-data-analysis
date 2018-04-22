package com.spike.giantdataanalysis.task.execution.core.activity;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadAssignor;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadCreator;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadExecutor;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * TaskActivity的工具类: 负责创建和销毁.
 * @author zhoujiagen
 */
@Service
public class TaskActivitys {

  private static final Logger LOG = LoggerFactory.getLogger(TaskActivitys.class);

  @Autowired
  private TaskExecutionProperties config;

  @Autowired
  private TaskStoreService taskStore;

  /** Map[组标识, 活动实体ID集合] */
  private Map<String, Set<String>> threads = Maps.newHashMap();

  @Autowired
  private Map<String, ApplicationWorkloadCreator> workloadCreators;

  @Autowired
  private Map<String, ApplicationWorkloadAssignor> workloadAssignors;

  @Autowired
  private Map<String, ApplicationWorkloadExecutor> workloadExecutors;

  // TODO(zhoujiagen) 改为使用反射方式创建???

  public TaskCreateActivity newTaskCreateActivity(String id) {
    LOG.info("创建任务创建活动: {}, id: {}", TaskCreateActivity.class.getSimpleName(), id);

    TaskCreateActivity taskActivity = new TaskCreateActivity(id, config, taskStore);
    ApplicationWorkloadCreator bean = null;
    for (String beanName : workloadCreators.keySet()) {
      bean = workloadCreators.get(beanName);
      bean.assignId(beanName);
      taskActivity.registWorkloadHandler(beanName, bean);
    }

    this.add(TaskCreateActivity.class.getSimpleName(), taskActivity);

    return taskActivity;
  }

  public TaskAssignmentActivity newTaskAssignmentActivity(String id) {
    LOG.info("创建任务指派活动: {}, id: {}", TaskAssignmentActivity.class.getSimpleName(), id);

    TaskAssignmentActivity taskActivity = new TaskAssignmentActivity(id, config, taskStore);
    ApplicationWorkloadAssignor bean = null;
    for (String beanName : workloadAssignors.keySet()) {
      bean = workloadAssignors.get(beanName);
      bean.assignId(beanName);
      taskActivity.registWorkloadHandler(beanName, bean);
    }

    this.add(TaskAssignmentActivity.class.getSimpleName(), taskActivity);

    return taskActivity;
  }

  public TaskExecuteActivity newTaskExecuteActivity(String id) {
    LOG.info("创建任务执行活动: {}, id: {}", TaskExecuteActivity.class.getSimpleName(), id);

    TaskExecuteActivity taskActivity = new TaskExecuteActivity(id, config, taskStore);
    ApplicationWorkloadExecutor bean = null;
    for (String beanName : workloadExecutors.keySet()) {
      bean = workloadExecutors.get(beanName);
      bean.assignId(beanName);
      taskActivity.registWorkloadHandler(beanName, bean);
    }

    this.add(TaskExecuteActivity.class.getSimpleName(), taskActivity);

    return taskActivity;
  }

  /** to work around to destroy representing java.lang.Thread */
  public void delete(TaskActivity taskActivity) {
    taskActivity.disable();

    this.remove(taskActivity.getClass().getSimpleName(), taskActivity);
  }

  public synchronized void add(String threadGroup, TaskActivity taskActivity) {
    if (StringUtils.isBlank(threadGroup)) {
      threadGroup = "DEFAULT";
    }
    Preconditions.checkArgument(taskActivity != null, "Invalid argument: taskActivity is null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(taskActivity.id()),
      "Invalid argument: taskActivity's id is null!");

    Set<String> aids = threads.get(threadGroup);
    if (aids == null) {
      aids = Sets.newHashSet();
    }
    aids.add(taskActivity.id());

    threads.put(threadGroup, aids);
  }

  public synchronized void remove(String threadGroup, TaskActivity taskActivity) {

    if (StringUtils.isBlank(threadGroup)) {
      threadGroup = "DEFAULT";
    }
    Preconditions.checkArgument(taskActivity != null, "Invalid argument: taskActivity is null!");
    Preconditions.checkArgument(StringUtils.isNotBlank(taskActivity.id()),
      "Invalid argument: taskActivity's id is null!");

    Set<String> aids = threads.get(threadGroup);
    if (aids == null) {
      return;
    }
    aids.remove(taskActivity.id());

    threads.put(threadGroup, aids);
  }

  public TaskExecutionProperties getConfig() {
    return config;
  }

  public void setConfig(TaskExecutionProperties config) {
    this.config = config;
  }

  public TaskStoreService getTaskStore() {
    return taskStore;
  }

  public void setTaskStore(TaskStoreService taskStore) {
    this.taskStore = taskStore;
  }

}
