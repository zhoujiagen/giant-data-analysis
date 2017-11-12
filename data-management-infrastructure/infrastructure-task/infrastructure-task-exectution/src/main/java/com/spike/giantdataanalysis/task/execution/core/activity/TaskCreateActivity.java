package com.spike.giantdataanalysis.task.execution.core.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.coordination.election.ElectionCoordination;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * 任务创建活动实体.
 * @author zhoujiagen
 */
public class TaskCreateActivity extends AbstractTaskActivity {
  private static final Logger LOG = LoggerFactory.getLogger(TaskCreateActivity.class);

  private TaskActivitys taskActivitys;
  @SuppressWarnings("unused")
  private TaskExecutionProperties config;
  @SuppressWarnings("unused")
  private TaskStoreService taskStore;
  @SuppressWarnings("unused")
  private ElectionCoordination electionCoordination;

  /** 集群中单实例 */
  private boolean singletonInCluster = true;

  public TaskCreateActivity(String id, TaskActivitys taskActivitys) {
    super(id);
    this.taskActivitys = taskActivitys;
    this.config = this.taskActivitys.getConfig();
    this.taskStore = this.taskActivitys.getTaskStore();
  }

  @Override
  public boolean enabled() throws TaskExecutionException {
    return singletonInCluster;
  }

  @Override
  public void initialize() throws TaskExecutionException {
    LOG.info("执行初始化工作 START");

    LOG.info("执行初始化工作 END");
  }

  @Override
  protected void prePlay() throws TaskExecutionException {
    LOG.info("{}-{}", this.getClass().getSimpleName(), "");

    try {
      Thread.sleep(2000l);
    } catch (InterruptedException e) {
      throw TaskExecutionException.newException(e);
    }
  }

  @Override
  protected void play() throws TaskExecutionException {
    LOG.info("{}-{}", this.getClass().getSimpleName(), "");
  }

  @Override
  protected void postPlay() throws TaskExecutionException {
    LOG.info("{}-{}", this.getClass().getSimpleName(), "");
  }

}
