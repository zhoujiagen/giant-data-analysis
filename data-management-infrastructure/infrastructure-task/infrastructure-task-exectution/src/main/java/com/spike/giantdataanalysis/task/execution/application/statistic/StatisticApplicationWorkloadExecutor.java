package com.spike.giantdataanalysis.task.execution.application.statistic;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadExecutor;
import com.spike.giantdataanalysis.task.execution.core.context.TaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.domain.TaskAssignment;
import com.spike.giantdataanalysis.task.store.domain.TaskExecution;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

@Service
public class StatisticApplicationWorkloadExecutor implements ApplicationWorkloadExecutor {
  private static final Logger LOG = LoggerFactory
      .getLogger(StatisticApplicationWorkloadExecutor.class);

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
    String myWorkerId = context.myWorkId();

    List<TaskAssignment> taskInfos = taskStoreService.myUnTakedTasks(myWorkerId);

    if (CollectionUtils.isEmpty(taskInfos)) {
      LOG.info("当前[{}]任务数量为0, 直接返回.", myWorkerId);
      return;
    }

    for (TaskAssignment tiwa : taskInfos) {
      TaskExecution te = new TaskExecution();
      te.setTaskInfoId(tiwa.getTaskId());
      te.setStartTime(new Date());
      te.setTaskAssignmentId(tiwa.getId());
      te.setTaskStatus(TaskExecution.TaskStatus.DOING);
      te = context.taskStoreService().updateTaskProgress(te);
      // 模拟负载
      LOG.info("{}[{}]处理负载: {}", this.getClass().getSimpleName(), id, tiwa);
      try {
        Thread.sleep(2000l);
        te.setTaskStatus(TaskExecution.TaskStatus.DONE);
      } catch (InterruptedException e) {
        te.setTaskStatus(TaskExecution.TaskStatus.CORRUPT);
        te.setTaskStatusInfo(this.getClass().getSimpleName() + "[" + id + "]处理负载失败");
        LOG.error("{}[{}]处理负载失败", this.getClass().getSimpleName(), id);
      } finally {
        te.setEndTime(new Date());
        context.taskStoreService().updateTaskProgress(te);
      }

    }
    long checkWorkPeriod = context.config().getExecutor().getCheckWorkPeriod();
    if (checkWorkPeriod > 0) {
      try {
        Thread.sleep(checkWorkPeriod);
      } catch (InterruptedException e) {
        throw TaskExecutionException.newException(e);
      }
    }
  }

}
