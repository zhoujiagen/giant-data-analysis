package com.spike.giantdataanalysis.task.execution.core.activity;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadHandler;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.context.DefaultTaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

abstract class AbstractTaskActivity implements TaskActivity {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractTaskActivity.class);

  protected String id;
  protected volatile boolean enabled = true;

  /** Map[Bean名称, 应用负载处理器] */
  protected Map<String, ApplicationWorkloadHandler> workloadHandlers = Maps.newConcurrentMap();

  protected TaskStoreService taskStoreService;
  protected TaskExecutionProperties config;
  /** 任务执行上下文 */
  protected DefaultTaskExecutionContext context;

  protected ObjectMapper objectMapper = new ObjectMapper();

  public AbstractTaskActivity(String id) {
    this.id = id;
  }

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public boolean enabled() throws TaskExecutionException {
    return enabled;
  }

  @Override
  public void disable() throws TaskExecutionException {
    this.enabled = false;
  }

  @Override
  public void run() {
    LOG.info("活动{}初始化.", this.getClass().getSimpleName());
    try {
      this.initialize();
    } catch (TaskExecutionException e) {
      LOG.error("活动初始化出现异常, 退出应用!", e);
      System.exit(-1);
    }

    LOG.info("活动{}启动.", this.getClass().getSimpleName());
    try {
      this.play();
    } catch (TaskExecutionException e) {
      LOG.error("活动执行出现异常", e);
    }
  }

  @Override
  public void registWorkloadHandler(String name, ApplicationWorkloadHandler workloadHandler)
      throws TaskExecutionException {
    if (StringUtils.isBlank(name) || !isValidWorkloadHandler(workloadHandler)) {
      throw TaskExecutionException.newException("Invalid argument: workloadHandler");
    }

    LOG.info("{}注册负载处理器: {}", this.getClass().getSimpleName(), workloadHandler.getClass()
        .getSimpleName());
    workloadHandlers.put(name, workloadHandler);
  }

  @Override
  public void unregistWorkloadHandler(String name) throws TaskExecutionException {
    if (StringUtils.isBlank(name)) {
      return;
    }

    LOG.info("{}取消注册负载处理器: {}", this.getClass().getSimpleName(), name);
    workloadHandlers.remove(name);
  }

  /**
   * 执行工作.
   * @throws TaskExecutionException
   */
  private void play() throws TaskExecutionException {
    while (true) {
      try {
        // 判断是否失效
        if (!this.enabled()) {
          LOG.info("活动{}已置为失效状态, 执行清理工作 START", this.getClass().getSimpleName());
          this.clean();
          LOG.info("活动{}已置为失效状态, 执行清理工作 END", this.getClass().getSimpleName());
          break;
        }

        // 执行工作
        this.doPlay();
      } catch (TaskExecutionException e) {
        LOG.error("执行工作失败", e);
      }

    }
  }

  /**
   * 实际执行的工作.
   * @throws TaskExecutionException
   */
  protected abstract void doPlay() throws TaskExecutionException;

}
