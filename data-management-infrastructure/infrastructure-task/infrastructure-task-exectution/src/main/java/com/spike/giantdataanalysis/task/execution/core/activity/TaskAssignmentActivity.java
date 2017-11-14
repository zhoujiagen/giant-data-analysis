package com.spike.giantdataanalysis.task.execution.core.activity;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.spike.giantdataanalysis.coordination.CoordinationRole;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.group.GroupCoordination;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadAssignor;
import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadHandler;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.context.DefaultTaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorDuty;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorInfo;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * 任务指派活动实体.
 * @author zhoujiagen
 */
public final class TaskAssignmentActivity extends AbstractTaskActivity {

  private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentActivity.class);

  // 群组协同
  private GroupCoordination groupCoordination;

  private ObjectMapper objectMapper = new ObjectMapper();

  private long checkWorkPeriod;

  public TaskAssignmentActivity(String id, TaskExecutionProperties config,
      TaskStoreService taskStoreService) {
    super(id);
    this.config = config;
    this.taskStoreService = taskStoreService;
  }

  @Override
  public void initialize() throws TaskExecutionException {
    LOG.info("执行初始化工作 START");

    checkWorkPeriod = config.getAssignor().getCheckWorkPeriod();
    context = new DefaultTaskExecutionContext();
    context.setConfig(config);
    context.setTaskStoreService(taskStoreService);

    String zookeeperConnectionString = config.getCoordination().getZookeeperConnectionString();
    String membershipPath = config.getCoordination().getMembershippath();
    String memberId = Coordinations.id();
    String memberDescription;

    TaskExecutorInfo tei = new TaskExecutorInfo();
    tei.setId(memberId);
    tei.setIps(Coordinations.getIps());
    tei.setRole(CoordinationRole.MASTER);
    tei.setDuty(TaskExecutorDuty.ASSIGNOR);
    try {
      memberDescription = objectMapper.writeValueAsString(tei);
    } catch (JsonProcessingException e) {
      memberDescription = memberId;
    }
    groupCoordination =
        new GroupCoordination(zookeeperConnectionString, membershipPath, memberId,
            memberDescription);
    groupCoordination.join();

    LOG.info("执行初始化工作 END");
  }

  /**
   * 获取当前可用的Worker ID集合.
   */
  private Set<String> getCurrentAvailableWorkers() {
    Set<String> result = Sets.newHashSet();

    Map<String, String> members = groupCoordination.getMembers();

    if (MapUtils.isNotEmpty(members)) {
      TaskExecutorInfo tei = null;
      for (String memberId : members.keySet()) {
        try {
          tei = objectMapper.readValue(members.get(memberId), TaskExecutorInfo.class);
        } catch (IOException e) {
          tei = null;
        }
        if (tei == null) {
          continue;
        }
        if (CoordinationRole.WORKER.name().equals(tei.getRole())) {
          result.add(memberId);
        }
      }
    }

    return result;
  }

  @Override
  public void clean() throws TaskExecutionException {
    groupCoordination.quit();
  }

  @Override
  protected void doPlay() throws TaskExecutionException {
    LOG.info("{}开始执行, 使用负载处理器: {}", this.getClass().getSimpleName(), workloadHandlers);

    // 获取当前可用的工作者
    context.setWorkers(this.getCurrentAvailableWorkers());

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
    if (workloadHandler == null || !(workloadHandler instanceof ApplicationWorkloadAssignor)) {
      return false;
    }

    return true;
  }

}
