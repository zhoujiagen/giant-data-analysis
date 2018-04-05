package com.spike.giantdataanalysis.task.execution.core.activity;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadCreator;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadHandler;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.context.DefaultTaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * 任务创建活动实体.
 * @author zhoujiagen
 */
public final class TaskCreateActivity extends AbstractTaskActivity {
  private static final Logger LOG = LoggerFactory.getLogger(TaskCreateActivity.class);

  private long checkWorkPeriod;

  public TaskCreateActivity(String id, TaskExecutionProperties config,
      TaskStoreService taskStoreService) {
    super(id);
    this.config = config;
    this.taskStoreService = taskStoreService;
  }

  @Override
  public void initialize() throws TaskExecutionException {
    LOG.info("执行初始化工作 START");

    checkWorkPeriod = config.getCreator().getCheckWorkPeriod();

    context = new DefaultTaskExecutionContext();
    context.setTaskStoreService(taskStoreService);
    context.setConfig(config);

    LOG.info("执行初始化工作 END");
  }

  @Override
  public void clean() throws TaskExecutionException {
  }

  @Override
  protected void doPlay() throws TaskExecutionException {
    LOG.info("{}开始执行, 使用负载处理器: {}", this.getClass().getSimpleName(), workloadHandlers);

    if (MapUtils.isNotEmpty(workloadHandlers)) {
      for (String id : workloadHandlers.keySet()) {
        workloadHandlers.get(id).handle(context);
      }
    }

    if (checkWorkPeriod > 0) {
      try {
        Thread.sleep(checkWorkPeriod);
      } catch (InterruptedException e) {
        throw TaskExecutionException.newException(e);
      }
    }
  }

  @Override
  public boolean isValidWorkloadHandler(ApplicationWorkloadHandler workloadHandler) {
    if (workloadHandler == null || !(workloadHandler instanceof ApplicationWorkloadCreator)) {
      return false;
    }

    return true;
  }

}
