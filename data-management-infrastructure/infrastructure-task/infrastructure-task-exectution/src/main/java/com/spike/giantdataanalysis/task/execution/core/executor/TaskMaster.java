package com.spike.giantdataanalysis.task.execution.core.executor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.coordination.CoordinationRole;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskActivity;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskActivitys;
import com.spike.giantdataanalysis.task.execution.core.threads.TaskThreads;

/**
 * 负责启动和停止[创建和指派]实际任务的工具.
 * @author zhoujiagen
 */
public class TaskMaster {

  private static final Logger LOG = LoggerFactory.getLogger(TaskMaster.class);

  private final TaskActivitys taskActivityFactory;

  private List<TaskActivity> taskActivities = Lists.newArrayList();
  private List<Thread> taskActivityThreads = Lists.newArrayList();

  private boolean canceled = false;

  public TaskMaster(TaskActivitys taskActivityFactory) {
    this.taskActivityFactory = taskActivityFactory;
  }

  public void start() {
    LOG.info("{}启动", this.getClass().getSimpleName());

    String id = Coordinations.id();
    TaskActivity taskCreateActivity = taskActivityFactory.newTaskCreateActivity(id);
    Thread taskCreateActivityThread =
        TaskThreads.I().newThread(CoordinationRole.MASTER.name(), "TaskCreateActivity-" + id,
          taskCreateActivity);
    taskActivityThreads.add(taskCreateActivityThread);
    TaskThreads.I().start(taskCreateActivityThread);

    id = Coordinations.id();
    TaskActivity taskAssignmentActivity = taskActivityFactory.newTaskAssignmentActivity(id);
    Thread taskAssignmentActivityThread =
        TaskThreads.I().newThread(CoordinationRole.MASTER.name(), "TaskAssignmentActivity-" + id,
          taskAssignmentActivity);
    taskActivityThreads.add(taskAssignmentActivityThread);
    TaskThreads.I().start(taskAssignmentActivityThread);
  }

  public void cancel() {
    LOG.info("{}被取消", this.getClass().getSimpleName());

    for (TaskActivity ta : taskActivities) {
      taskActivityFactory.delete(ta);
    }
    taskActivityThreads.clear();

    canceled = true;
  }

  public boolean isCanceled() {
    return canceled;
  }

  public void join() {
    for (Thread t : taskActivityThreads) {
      TaskThreads.I().join(t);
    }
  }

}
