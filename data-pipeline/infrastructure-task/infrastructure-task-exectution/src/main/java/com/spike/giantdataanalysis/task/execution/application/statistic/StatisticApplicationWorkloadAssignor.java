package com.spike.giantdataanalysis.task.execution.application.statistic;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadAssignor;
import com.spike.giantdataanalysis.task.execution.core.context.TaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.domain.TaskAssignment;
import com.spike.giantdataanalysis.task.store.domain.TaskInfo;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

@Service
public class StatisticApplicationWorkloadAssignor implements ApplicationWorkloadAssignor {

  private static final Logger LOG = LoggerFactory
      .getLogger(StatisticApplicationWorkloadAssignor.class);

  private String id;

  @Override
  public void assignId(String id) {
    this.id = id;
  }

  @Transactional(rollbackOn = { TaskExecutionException.class })
  @Override
  public void handle(TaskExecutionContext context) throws TaskExecutionException {
    LOG.info("{}[{}]处理负载", this.getClass().getSimpleName(), id);

    TaskStoreService taskStoreService = context.taskStoreService();
    Set<String> workers = context.workers();
    LOG.info("当前可用工作者: {}", workers);
    if (workers.size() == 0) {
      LOG.info("当前无工作者可用, 直接返回.");
      return;
    }

    List<TaskInfo> unassignedTasks = taskStoreService.queryUnassignedTask();
    LOG.info("当前未指派的任务定义: {}", unassignedTasks);
    if (CollectionUtils.isEmpty(unassignedTasks)) {
      LOG.info("当前无任务指派, 直接返回.");
      return;
    }
    // 待指派的任务定义
    List<TaskInfo> toAssignTasks = Lists.newArrayList();

    List<TaskInfo> runningTasks = taskStoreService.queryRunningTasks();
    LOG.info("当前在运行的任务定义: {}", runningTasks);
    if (CollectionUtils.isEmpty(runningTasks)) {

      toAssignTasks.addAll(unassignedTasks);

    } else {

      Map<String, List<TaskInfo>> wskTaskListMap = Maps.newHashMap();
      List<TaskInfo> tempTaskInfos = null;
      for (TaskInfo taskInfo : unassignedTasks) {
        tempTaskInfos = wskTaskListMap.get(taskInfo.getWorkloadShardKey());
        if (tempTaskInfos == null) {
          tempTaskInfos = Lists.newArrayList();
        }
        tempTaskInfos.add(taskInfo);

        wskTaskListMap.put(taskInfo.getWorkloadShardKey(), tempTaskInfos);
      }

      Set<String> runningWsks = Sets.newHashSet();
      for (TaskInfo runningTask : runningTasks) {
        runningWsks.add(runningTask.getWorkloadShardKey());
      }

      for (String wsk : wskTaskListMap.keySet()) {
        // 当前对应负载键无任务在执行时, 不加入待指派任务定义中
        if (runningTasks.contains(wsk)) {
          continue;
        }

        List<TaskInfo> toAssignTasksThisWsk = wskTaskListMap.get(wsk);
        if (CollectionUtils.isEmpty(toAssignTasksThisWsk)) {
          continue;
        }

        // 容错处理, 应对单个负载键下多个任务
        // (1) 如果同时存在REGUALR和REDO, 加入首个REDO
        // (2) 如果同时存在多个REGULAR, 加入首个REGUALR
        TaskInfo toAddTask = this.firstTask(toAssignTasksThisWsk, TaskInfo.TaskType.REDO);
        if (toAddTask != null) {
          toAssignTasks.add(toAddTask);
          continue;
        } else {
          toAddTask = this.firstTask(toAssignTasksThisWsk, TaskInfo.TaskType.REGUALR);
          if (toAddTask != null) {
            toAssignTasks.add(toAddTask);
            continue;
          }
        }
      }
    }

    if (CollectionUtils.isEmpty(toAssignTasks)) {
      LOG.info("查询当前运行任务后, 确认当前无任务指派, 直接返回.");
      return;
    }

    Random random = new Random(new Date().getTime());
    for (TaskInfo ti : toAssignTasks) {
      TaskAssignment ta = new TaskAssignment();
      ta.setTaskId(ti.getId());
      // 随机选择工作者
      // TODO(zhoujiagen) 统计工作者的效率
      ta.setWorkerId(Lists.newArrayList(workers).get(random.nextInt(workers.size())));

      taskStoreService.assignTask(ta);
    }

    try {
      Thread.sleep(2000l);
    } catch (InterruptedException e) {
      // ignore
    }

    long checkWorkPeriod = context.config().getAssignor().getCheckWorkPeriod();
    if (checkWorkPeriod > 0) {
      try {
        Thread.sleep(checkWorkPeriod);
      } catch (InterruptedException e) {
        throw TaskExecutionException.newException(e);
      }
    }
  }

  private TaskInfo firstTask(List<TaskInfo> taskInfos, TaskInfo.TaskType taskType) {
    for (TaskInfo taskInfo : taskInfos) {
      if (taskType.equals(taskInfo.getTaskType())) {
        return taskInfo;
      }
    }
    return null;
  }

}
