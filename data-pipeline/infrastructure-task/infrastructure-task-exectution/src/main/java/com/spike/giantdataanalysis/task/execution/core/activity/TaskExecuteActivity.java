package com.spike.giantdataanalysis.task.execution.core.activity;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.collections4.MapUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.coordination.CoordinationRole;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.group.GroupCoordination;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadExecutor;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadHandler;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.context.DefaultTaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.core.context.TaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorDuty;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorInfo;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * 任务执行活动实体.
 * @author zhoujiagen
 */
public final class TaskExecuteActivity extends AbstractTaskActivity {
  private static final Logger LOG = LoggerFactory.getLogger(TaskExecuteActivity.class);

  /** 工作者槽 */
  private int slots;
  private ExecutorService es;

  /** 群组协同 */
  private List<GroupCoordination> groupCoordinations = Lists.newArrayList();
  private List<String> workerGroupMemberIds = Lists.newArrayList();

  private long checkWorkPeriod;

  public TaskExecuteActivity(String id, TaskExecutionProperties config,
      TaskStoreService taskStoreService) {
    super(id);
    this.config = config;
    this.taskStoreService = taskStoreService;
  }

  @Override
  public void initialize() throws TaskExecutionException {
    LOG.info("执行初始化工作 START");

    checkWorkPeriod = config.getExecutor().getCheckWorkPeriod();
    context = new DefaultTaskExecutionContext();
    context.setTaskStoreService(taskStoreService);
    context.setConfig(config);

    String zookeeperConnectionString = config.getCoordination().getZookeeperConnectionString();
    String membershipPath = config.getCoordination().getMembershippath();

    slots = config.getExecutor().getSlots();
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
      workerGroupMemberIds.add(memberId);
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
      GroupCoordination groupCoordination =
          new GroupCoordination(client, membershipPath, memberId, memberDescription);
      groupCoordination.join();
      groupCoordinations.add(groupCoordination);
    }

    es = Executors.newFixedThreadPool(slots);
    LOG.info("执行初始化工作 END");
  }

  @Override
  public void clean() throws TaskExecutionException {
    for (GroupCoordination gc : groupCoordinations) {
      gc.quit();
    }
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

    for (String beanName : workloadHandlers.keySet()) {
      final ApplicationWorkloadHandler workloadHandler = workloadHandlers.get(beanName);

      try {
        for (String workerId : workerGroupMemberIds) {
          context.setMyWorkId(workerId);
          es.execute(new ApplicationWorkloadHandlerWorker(workerId, workloadHandler, context, cdl));
        }

        cdl.await();

      } catch (RejectedExecutionException e) {
        LOG.error("提交任务被拒绝", e);
        throw TaskExecutionException.newException(e);

      } catch (InterruptedException e) {
        LOG.error("提交任务后等待完成失败", e);
        throw TaskExecutionException.newException(e);
      }
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

  private static class ApplicationWorkloadHandlerWorker implements Runnable {
    private final ApplicationWorkloadHandler workloadHandler;
    private final TaskExecutionContext context;
    private final CountDownLatch cdl;

    public ApplicationWorkloadHandlerWorker(String workerId,
        ApplicationWorkloadHandler workloadHandler, TaskExecutionContext context, CountDownLatch cdl) {
      this.workloadHandler = workloadHandler;
      this.context = context;
      this.cdl = cdl;
    }

    @Override
    public void run() {

      try {
        workloadHandler.handle(context);
      } catch (TaskExecutionException e) {
        LOG.error("处理失败", e);
      } finally {
        cdl.countDown();
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
