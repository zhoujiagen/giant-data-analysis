package com.spike.giantdataanalysis.task.execution.core.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;

public abstract class AbstractTaskActivity implements TaskActivity {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractTaskActivity.class);

  protected String id;

  public AbstractTaskActivity(String id) {
    this.id = id;
  }

  @Override
  public String id() {
    return this.id;
  }

  /** 空实现. */
  @Override
  public void initialize() throws TaskExecutionException {
  }

  /** 空实现. */
  @Override
  public void clean() throws TaskExecutionException {
  }

  /** 默认有效. */
  @Override
  public boolean enabled() throws TaskExecutionException {
    return true;
  }

  /** 空实现. */
  @Override
  public void disable() throws TaskExecutionException {
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

    while (true) {

      try {
        LOG.debug("执行工作前工作");
        this.prePlay();

        LOG.debug("执行工作");
        this.play();

        LOG.debug("执行工作后工作");
        this.postPlay();
      } catch (TaskExecutionException e) {
        LOG.error("执行工作出现异常", e);
      }

    }
  }

  /**
   * 执行工作前工作.
   * @throws TaskExecutionException
   */
  protected abstract void prePlay() throws TaskExecutionException;

  /**
   * 执行工作.
   * @throws TaskExecutionException
   */
  protected abstract void play() throws TaskExecutionException;

  /**
   * 执行工作后工作.
   * @throws TaskExecutionException
   */
  protected abstract void postPlay() throws TaskExecutionException;

}
