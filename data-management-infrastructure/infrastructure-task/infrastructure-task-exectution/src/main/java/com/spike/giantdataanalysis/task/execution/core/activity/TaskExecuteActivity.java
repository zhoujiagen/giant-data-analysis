package com.spike.giantdataanalysis.task.execution.core.activity;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spike.giantdataanalysis.coordination.CoordinationRole;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.group.GroupCoordination;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorDuty;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorInfo;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * 任务执行活动实体.
 * @author zhoujiagen
 */
public class TaskExecuteActivity extends AbstractTaskActivity {
  private static final Logger LOG = LoggerFactory.getLogger(TaskExecuteActivity.class);

  private TaskActivitys taskActivitys;
  private TaskExecutionProperties config;
  @SuppressWarnings("unused")
  private TaskStoreService taskStore;

  /** 工作者槽 */
  private int slots;
  private ExecutorService es;

  /** 群组协同 */
  private GroupCoordination groupCoordination;

  private ObjectMapper objectMapper = new ObjectMapper();

  public TaskExecuteActivity(String id, TaskActivitys taskActivitys) {
    super(id);
    this.taskActivitys = taskActivitys;
    this.config = this.taskActivitys.getConfig();
    this.taskStore = this.taskActivitys.getTaskStore();
  }

  @Override
  public void initialize() throws TaskExecutionException {
    LOG.info("执行初始化工作 START");

    String zookeeperConnectionString = config.getCoordination().getZookeeperConnectionString();
    String membershipPath = config.getCoordination().getMembershippath();
    String memberId = Coordinations.id();
    String memberDescription;
    TaskExecutorInfo tei = new TaskExecutorInfo();
    tei.setId(memberId);
    tei.setIps(Coordinations.getIps());
    tei.setRole(CoordinationRole.WORKER);
    tei.setDuty(TaskExecutorDuty.EXECUTOR);
    try {
      memberDescription = objectMapper.writeValueAsString(tei);
    } catch (JsonProcessingException e) {
      memberDescription = memberId;
    }
    groupCoordination =
        new GroupCoordination(zookeeperConnectionString, membershipPath, memberId,
            memberDescription);
    groupCoordination.join();

    slots = config.getWorker().getSlots();
    LOG.info("TaskExecuteActivity slots: {}", slots);
    if (slots <= 0) {
      LOG.warn("slot[{}] <= 0!", slots);
      return;
    }

    es = Executors.newFixedThreadPool(slots);
    LOG.info("执行初始化工作 END");
  }

  @Override
  public void clean() throws TaskExecutionException {
    groupCoordination.quit();
    // Initiates an orderly shutdown in which previously submitted tasks are executed,
    // but no new tasks will be accepted.
    es.shutdown();
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

    es.execute(new Runnable() {

      @Override
      public void run() {
        System.err.println("hello work!!! " + new Date().getTime());
      }
    });

  }

  @Override
  protected void postPlay() throws TaskExecutionException {
    LOG.info("{}-{}", this.getClass().getSimpleName(), "");
  }

}
