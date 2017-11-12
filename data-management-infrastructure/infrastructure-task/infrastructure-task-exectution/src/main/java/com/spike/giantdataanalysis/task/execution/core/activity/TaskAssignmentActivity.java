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
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorDuty;
import com.spike.giantdataanalysis.task.execution.core.executor.TaskExecutorInfo;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

/**
 * 任务指派活动实体.
 * @author zhoujiagen
 */
public class TaskAssignmentActivity extends AbstractTaskActivity {

  private static final Logger LOG = LoggerFactory.getLogger(TaskAssignmentActivity.class);

  private TaskActivitys taskActivitys;
  private TaskExecutionProperties config;
  @SuppressWarnings("unused")
  private TaskStoreService taskStore;

  // 群组协同
  private GroupCoordination groupCoordination;

  private ObjectMapper objectMapper = new ObjectMapper();

  public TaskAssignmentActivity(String id, TaskActivitys taskActivitys) {
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
  @SuppressWarnings("unused")
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
