package com.spike.giantdataanalysis.task.execution.core.executor;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.spike.giantdataanalysis.coordination.CoordinationRole;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.election.ElectionCoordination;
import com.spike.giantdataanalysis.coordination.exception.CoordinationException;
import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskActivitys;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskAssignmentActivity;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskCreateActivity;
import com.spike.giantdataanalysis.task.execution.core.activity.TaskExecuteActivity;
import com.spike.giantdataanalysis.task.execution.core.threads.TaskThreads;

/**
 * 任务执行者: 扮演Master, Worker.
 * @author zhoujiagen
 */
@Service
public class TaskExecutor implements ApplicationListener<ApplicationReadyEvent> {
  private static final Logger LOG = LoggerFactory.getLogger(TaskExecutor.class);

  @Autowired
  private TaskExecutionProperties config;

  @Autowired
  private TaskActivitys taskActivityFactory;

  /** 是否是Master */
  private boolean isLeader = false;

  /** 集群中单实例 */
  private boolean isSingletonInCluster = true;
  private ElectionCoordination electionCoordination;

  private TaskMaster taskMaster;
  private TaskWorker taskWorker;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    // 启动背景守护线程
    long checkAlivePeriod = config.getCheckAlivePeriod();
    LOG.info("checkAlivePeriod={}", checkAlivePeriod);
    TaskThreads.I().startBackgroundDaemon(checkAlivePeriod);

    // 确认扮演的角色
    this.determineRole();

    if (isLeader) {

      LOG.info("扮演的角色: {}, {}.", TaskCreateActivity.class.getSimpleName(),
        TaskAssignmentActivity.class.getSimpleName());

      // 处理active与standby两种角色的转换
      isSingletonInCluster = config.isSingletonInCluster();
      LOG.info("isSingletonInCluster={}", isSingletonInCluster);
      if (isSingletonInCluster) {
        String zookeeperConnectionString = config.getCoordination().getZookeeperConnectionString();
        String leadershipPath = config.getCoordination().getLeadershippath();
        String memberId = Coordinations.id();
        electionCoordination =
            new ElectionCoordination(zookeeperConnectionString, leadershipPath, memberId) {
              @Override
              public void playAsLeader(CuratorFramework client) throws CoordinationException {
                if (taskMaster == null) {
                  taskMaster = new TaskMaster(taskActivityFactory);
                  taskMaster.start();
                  taskMaster.join();
                }
                while (!taskMaster.isCanceled()) {
                  try {
                    Thread.sleep(1000l);
                  } catch (InterruptedException e) {
                    // ignore
                  }
                }
              }

              @Override
              public void palyAsFollower(LeaderSelector leaderSelector, CuratorFramework client)
                  throws CoordinationException {
                while (true) {
                  if (leaderSelector.hasLeadership()) {
                    // take over leader work
                    taskMaster = new TaskMaster(taskActivityFactory);
                    taskMaster.start();
                    taskMaster.join();

                    break;
                  }
                  try {
                    LOG.debug("Stand by...");
                    Thread.sleep(2000l);
                  } catch (InterruptedException e) {
                    // ignore
                  }
                }

              }

              @Override
              public void
                  handleLeaderStateChanged(CuratorFramework client, ConnectionState newState) {
                if (client.getConnectionStateErrorPolicy().isErrorState(newState)) {
                  // cancel leader current work
                  taskMaster.cancel();
                  throw new CancelLeadershipException();
                }
              }
            };

        electionCoordination.blockingCheckLeadership();
      }

    } else {

      LOG.info("扮演的角色: {}.", TaskExecuteActivity.class.getSimpleName());
      taskWorker = new TaskWorker(taskActivityFactory);
      taskWorker.start();
    }
  }

  // 确认需扮演的角色
  private void determineRole() {
    LOG.info("确认需扮演的角色 START");

    CoordinationRole role = config.getCoordinationRole();
    if (role == null) {
      LOG.warn("未知的角色配置: {}, 可选角色: {}. 退出应用!", config.getRole(), CoordinationRole.values());
      System.exit(-1);
    }
    if (CoordinationRole.MASTER.equals(role)) {
      isLeader = true;
    } else {
      isLeader = false;
    }

    LOG.info("确认需扮演的角色 END: result={}", role);
  }

}
