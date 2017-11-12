package com.spike.giantdataanalysis.task.execution.core.activity;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * TaskActivity的工具类: 负责创建和销毁.
 * @author zhoujiagen
 */
@Service
public class TaskActivitys {

  @Autowired
  private TaskExecutionProperties config;

  @Autowired
  private TaskStoreService taskStore;

  /** Map[组标识, 活动实体ID集合] */
  private Map<String, Set<String>> threads = Maps.newHashMap();

  public TaskCreateActivity newTaskCreateActivity(String id) {
    TaskCreateActivity taskActivity = new TaskCreateActivity(id, this);
    this.add(TaskCreateActivity.class.getSimpleName(), taskActivity);
    return taskActivity;
  }

  public TaskAssignmentActivity newTaskAssignmentActivity(String id) {
    TaskAssignmentActivity taskActivity = new TaskAssignmentActivity(id, this);
    this.add(TaskAssignmentActivity.class.getSimpleName(), taskActivity);
    return taskActivity;
  }

  public TaskExecuteActivity newTaskExecuteActivity(String id) {
    TaskExecuteActivity taskActivity = new TaskExecuteActivity(id, this);
    this.add(TaskExecuteActivity.class.getSimpleName(), taskActivity);
    return taskActivity;
  }

  /** to work around to destroy representing java.lang.Thread */
  public void delete(TaskActivity taskActivity) {
    taskActivity.disable();
    taskActivity.clean();

    // if (taskActivity instanceof TaskCreateActivity) {
    // this.remove(TaskCreateActivity.class.getSimpleName(), taskActivity);
    // } else if (taskActivity instanceof TaskAssignmentActivity) {
    // this.remove(TaskAssignmentActivity.class.getSimpleName(), taskActivity);
    // } else if (taskActivity instanceof TaskExecuteActivity) {
    // this.remove(TaskExecuteActivity.class.getSimpleName(), taskActivity);
    // }
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
