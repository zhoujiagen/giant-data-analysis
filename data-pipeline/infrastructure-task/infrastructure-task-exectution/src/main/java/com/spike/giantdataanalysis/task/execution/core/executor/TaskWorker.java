package com.spike.giantdataanalysis.task.execution.core.executor;

import java.util.List;

import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.coordination.CoordinationRole;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskActivity;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskActivitys;
import com.spike.giantdataanalysis.task.execution.core.threads.TaskThreads;

/**
 * 负责启动和停止[执行]实际任务的工具.
 * @author zhoujiagen
 */
public class TaskWorker {

  private final TaskActivitys taskActivityFactory;

  private List<TaskActivity> taskActivities = Lists.newArrayList();
  private List<Thread> taskActivityThreads = Lists.newArrayList();

  public TaskWorker(TaskActivitys taskActivityFactory) {
    this.taskActivityFactory = taskActivityFactory;
  }

  public void start() {
    String id = Coordinations.id();
    TaskActivity taskExecuteActivity = taskActivityFactory.newTaskExecuteActivity(id);
    taskActivities.add(taskExecuteActivity);
    Thread taskExecuteActivityThread =
        TaskThreads.I().newThread(CoordinationRole.WORKER.name(), "TaskExecuteActivity-" + id,
          taskExecuteActivity);
    taskActivityThreads.add(taskExecuteActivityThread);
    TaskThreads.I().start(taskExecuteActivityThread);
  }

  public void cancel() {
    for (TaskActivity ta : taskActivities) {
      taskActivityFactory.delete(ta);
    }
    taskActivityThreads.clear();
  }

  public void join() {
    for (Thread t : taskActivityThreads) {
      TaskThreads.I().join(t);
    }
  }
}
