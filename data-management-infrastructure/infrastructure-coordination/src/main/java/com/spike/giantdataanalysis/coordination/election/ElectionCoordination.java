package com.spike.giantdataanalysis.coordination.election;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.leader.Participant;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.exception.CoordinationException;

/**
 * Leader选举协同
 * @author zhoujiagen
 */
public abstract class ElectionCoordination {
  private static final Logger LOG = LoggerFactory.getLogger(ElectionCoordination.class);

  protected String zookeeperConnectionString;
  protected String leadershipPath;
  protected String memberId;

  private CuratorFramework client;
  private LeaderSelector leaderSelector;

  // /** 当前实例是否是Leader */
  // private boolean isLeader;

  /**
   * Leader选举协同
   * @param zookeeperConnectionString ZK连接串
   * @param leadershipPath 使用的ZNode路径
   * @param memberId 成员标识
   */
  public ElectionCoordination(String zookeeperConnectionString, String leadershipPath,
      String memberId) {

    Preconditions.checkArgument(StringUtils.isNotBlank(zookeeperConnectionString), "ZK连接串不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(leadershipPath), " 使用的ZNode路径不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(memberId), "成员标识不可为空!");

    this.zookeeperConnectionString = zookeeperConnectionString;
    this.leadershipPath = leadershipPath;
    this.memberId = memberId;

    this.init();
  }

  /**
   * Leader选举协同
   * @param client Curator客户端
   * @param leadershipPath 使用的ZNode路径
   * @param memberId 成员标识
   */
  public ElectionCoordination(CuratorFramework client, String leadershipPath, String memberId) {
    Preconditions.checkArgument(client != null, "Curator客户端不可为空!");
    Preconditions.checkArgument(!CuratorFrameworkState.STOPPED.equals(client.getState()),
      "Curator客户端已停止!");
    Preconditions.checkArgument(StringUtils.isNotBlank(leadershipPath), " 使用的ZNode路径不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(memberId), "成员标识不可为空!");

    this.client = client;
    this.zookeeperConnectionString = client.getZookeeperClient().getCurrentConnectionString();
    this.leadershipPath = leadershipPath;
    this.memberId = memberId;

    this.init();
  }

  /**
   * 执行Leader工作.
   * <p>
   * 空实现
   * @param client
   * @throws CoordinationException
   */
  protected void playAsLeader(CuratorFramework client) throws CoordinationException {
  }

  /**
   * 处理Leader的状态改变.
   * @param client
   * @param newState
   */
  protected void handleLeaderStateChanged(CuratorFramework client, ConnectionState newState) {
    // (state == ConnectionState.SUSPENDED) || (state == ConnectionState.LOST)
    if (client.getConnectionStateErrorPolicy().isErrorState(newState)) {
      throw new CancelLeadershipException();
    }
  }

  /**
   * 执行Follower工作.
   * @param leaderSelector
   * @param client
   * @throws CoordinationException
   */
  protected abstract void palyAsFollower(LeaderSelector leaderSelector, CuratorFramework client)
      throws CoordinationException;

  // 初始化LeaderSelector, 阻塞判断Leader关系.
  private void init() {
    // 客户端
    if (client == null) {
      client =
          CuratorFrameworkFactory.newClient(zookeeperConnectionString,
            Coordinations.DEFAULT_CURATOR_RETRY_POLICY);
      client.start();
    } else {
      if (CuratorFrameworkState.LATENT.equals(client.getState())) {
        client.start();
      }
    }

    LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
      @Override
      public void stateChanged(CuratorFramework client, ConnectionState newState) {
        handleLeaderStateChanged(client, newState);
      }

      @Override
      public void takeLeadership(CuratorFramework client) throws Exception {
        LOG.info("Well, I am the leader!");
        playAsLeader(client);
      }
    };
    leaderSelector = new LeaderSelector(client, leadershipPath, listener);
    leaderSelector.setId(memberId);
    leaderSelector.autoRequeue();
    leaderSelector.start();
  }

  /**
   * 阻塞判断Leader关系.
   * @param client
   */
  public boolean blockingCheckLeadership() throws CoordinationException {

    boolean isLeader = false;

    while (true) {

      try {
        Participant leader = leaderSelector.getLeader();

        if (!leader.isLeader()) {

          LOG.info("{}: Current no leader found, wait a few seconds...", memberId);
          Thread.sleep(1000l);// ~

        } else {

          LOG.info("{}: Leader found, its id is {}: same? {}", memberId, leader.getId(),
            memberId.equals(leader.getId()));
          if (memberId.equals(leader.getId())) {
            isLeader = true;
          }
          break;
        }

      } catch (Exception e) {
        LOG.error("blockingCheckLeadership failed", e);
      }
    }

    if (!isLeader) {
      LOG.info("Well, I am the follower!");
      this.palyAsFollower(leaderSelector, client);
    }

    return isLeader;
  }
}
