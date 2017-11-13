package com.spike.giantdataanalysis.task.execution.core.activity;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections4.MapUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spike.giantdataanalysis.coordination.CoordinationRole;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.group.GroupCoordination;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadExecutor;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadHandler;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorDuty;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorInfo;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;

/**
 * 任务执行活动实体.
 * @author zhoujiagen
 */
public class TaskExecuteActivity extends AbstractTaskActivity {
  private static final Logger LOG = LoggerFactory.getLogger(TaskExecuteActivity.class);

  private TaskExecutionProperties config;

  /** 工作者槽 */
  private int slots;
  private ExecutorService es;

  /** 群组协同 */
  private GroupCoordination groupCoordination;

  private ObjectMapper objectMapper = new ObjectMapper();

  private long checkWorkPeriod;

  public TaskExecuteActivity(String id, TaskExecutionProperties config) {
    super(id);
    this.config = config;
  }

  @Override
  public void initialize() throws TaskExecutionException {
    LOG.info("执行初始化工作 START");

    checkWorkPeriod = config.getExecutorConfig().getCheckWorkPeriod();

    String zookeeperConnectionString = config.getCoordination().getZookeeperConnectionString();
    String membershipPath = config.getCoordination().getMembershippath();

    slots = config.getExecutorConfig().getSlots();
    LOG.info("TaskExecuteActivity slots: {}", slots);
    if (slots <= 0) {
      LOG.warn("slot[{}] <= 0!", slots);
      throw TaskExecutionException.newException("negative executor slots: " + slots);
    }

    CuratorFramework client =
        Coordinations.curatorClient(zookeeperConnectionString,
          Coordinations.DEFAULT_CURATOR_RETRY_POLICY);
    for (int i = 0; i < slots; i++) {
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
          new GroupCoordination(client, membershipPath, memberId, memberDescription);
      groupCoordination.join();
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
  protected void doPlay() throws TaskExecutionException {
    LOG.info("{}开始执行, 使用负载处理器: {}", this.getClass().getSimpleName(), workloadHandlers);

    if (MapUtils.isEmpty(workloadHandlers)) {
      return;
    }

    // wait current round to finish
    final CountDownLatch cdl = new CountDownLatch(workloadHandlers.size());

    for (String id : workloadHandlers.keySet()) {
      final ApplicationWorkloadHandler workloadHandler = workloadHandlers.get(id);

      es.execute(new Runnable() {
        @Override
        public void run() {
          try {
            workloadHandler.handle();
          } catch (TaskExecutionException e) {
            LOG.error("处理失败", e);
          } finally {
            cdl.countDown();
          }
        }
      });
    }

    try {
      cdl.await();
    } catch (InterruptedException e) {
      throw TaskExecutionException.newException(e);
    }

    LOG.info("{}结束执行", this.getClass().getSimpleName());

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
    if (workloadHandler == null || !(workloadHandler instanceof ApplicationWorkloadExecutor)) {
      return false;
    }

    return true;
  }

}
