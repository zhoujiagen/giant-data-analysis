package com.spike.giantdataanalysis.task.execution.application.statistic;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadCreator;
import com.spike.giantdataanalysis.task.execution.core.context.TaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.domain.TaskExecution;
import com.spike.giantdataanalysis.task.store.domain.TaskInfo;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

@Service
public class StatisticApplicationWorkloadCreator implements ApplicationWorkloadCreator {
  private static final Logger LOG = LoggerFactory
      .getLogger(StatisticApplicationWorkloadCreator.class);

  private String id;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void assignId(String id) {
    this.id = id;
  }

  @SuppressWarnings("unchecked")
  @Transactional(rollbackOn = { TaskExecutionException.class })
  @Override
  public void handle(TaskExecutionContext context) throws TaskExecutionException {
    LOG.info("{}[{}]处理负载", this.getClass().getSimpleName(), id);

    Set<String> workloadIds = StatisticDataFactory.I().getWorkloadIds(10);

    long initStart = context.config().getCreator().getInitStart();
    long initEnd = context.config().getCreator().getInitEnd();

    List<TaskInfo> lastTaskInfos = context.taskStoreService().queryLastTaskInfo(workloadIds);

    if (CollectionUtils.isEmpty(lastTaskInfos)) {
      // 创建初始化的任务定义
      if (CollectionUtils.isNotEmpty(workloadIds)) {
        LOG.info("创建初始化的任务定义: {}", workloadIds);
        for (String workloadId : workloadIds) {
          this.createTaskInfo(context.taskStoreService(), workloadId, initStart, initEnd);
        }
      }
      return;
    }

    if (CollectionUtils.isNotEmpty(lastTaskInfos)) {
      Map<String, TaskInfo> map = Maps.newHashMap();
      for (TaskInfo taskInfo : lastTaskInfos) {
        map.put(taskInfo.getWorkloadShardKey(), taskInfo);
      }

      Set<String> existedWsk = map.keySet();
      workloadIds.removeAll(existedWsk);
      if (CollectionUtils.isNotEmpty(workloadIds)) {
        for (String newWorkloadId : workloadIds) {
          // 创建初始化的任务定义
          this.createTaskInfo(context.taskStoreService(), newWorkloadId, initStart, initEnd);
        }
      }

      TaskInfo tempTaskInfo = null;
      boolean finishedFlag = false;
      for (String wsk : map.keySet()) {
        tempTaskInfo = map.get(wsk);
        finishedFlag =
            TaskExecution.TaskStatus.DONE.equals(tempTaskInfo.getTaskStatus())
                || TaskExecution.TaskStatus.CORRUPT.equals(tempTaskInfo.getTaskStatus());
        if (!finishedFlag) {
          continue;
        }

        // 创建新任务定义
        TaskInfo taskInfo = map.get(wsk);
        Map<String, String> workloadShardInfo = null;
        try {
          workloadShardInfo = objectMapper.readValue(taskInfo.getWorkloadShardInfo(), Map.class);
        } catch (IOException e) {
          LOG.error("处理Last TaskInfo[" + taskInfo + "]失败", e);
          continue;
        }
        long start = Long.valueOf(workloadShardInfo.get("start"));
        long end = Long.valueOf(workloadShardInfo.get("end"));

        // TODO(zhoujiagen) 处理任务之间的依赖关系, 以确定下一批次的任务
        start = end;
        end = end + 10000l;

        this.createTaskInfo(context.taskStoreService(), wsk, start, end);
      }
    }

    long checkWorkPeriod = context.config().getCreator().getCheckWorkPeriod();
    if (checkWorkPeriod > 0) {
      try {
        Thread.sleep(checkWorkPeriod);
      } catch (InterruptedException e) {
        throw TaskExecutionException.newException(e);
      }
    }
  }

  private void createTaskInfo(TaskStoreService taskStoreService, String workloadShardKey,
      long start, long end) {
    TaskInfo taskInfo = new TaskInfo();
    taskInfo.setWorkloadShardKey(workloadShardKey);
    Map<String, String> workloadShardInfo = new HashMap<String, String>();
    workloadShardInfo.put("start", String.valueOf(start));
    workloadShardInfo.put("end", String.valueOf(end));

    try {
      taskInfo.setWorkloadShardInfo(objectMapper.writeValueAsString(workloadShardInfo));
    } catch (JsonProcessingException e) {
      taskInfo.setWorkloadShardInfo("UNKNOWN");
    }
    taskStoreService.createTask(taskInfo);
  }

}
