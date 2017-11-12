package com.spike.giantdataanalysis.coordination.group;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.nodes.GroupMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.spike.giantdataanalysis.coordination.Coordinations;

/**
 * 群组协同
 * @author zhoujiagen
 */
public class GroupCoordination {

  private static final Logger LOG = LoggerFactory.getLogger(GroupCoordination.class);

  private final String zookeeperConnectionString;
  private final String membershipPath;
  private final String memberId;
  private final String memberDescription;

  private CuratorFramework client;
  private GroupMember groupMember;

  /**
   * 群组协同
   * @param zookeeperConnectionString ZK连接串
   * @param membershipPath 使用的组ZNode路径
   * @param memberId 成员标识
   * @param memberDescription 成员描述
   */
  public GroupCoordination(String zookeeperConnectionString, String membershipPath,
      String memberId, String memberDescription) {

    Preconditions.checkArgument(StringUtils.isNotBlank(zookeeperConnectionString), "ZK连接串不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(membershipPath), "使用的组ZNode路径不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(memberId), "成员标识不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(memberDescription), " 成员描述不可为空!");

    this.zookeeperConnectionString = zookeeperConnectionString;
    this.membershipPath = membershipPath;
    this.memberId = memberId;
    this.memberDescription = memberDescription;

    this.init();
  }

  public GroupCoordination(CuratorFramework client, String membershipPath, String memberId,
      String memberDescription) {
    Preconditions.checkArgument(client != null, "Curator客户端不可为空!");
    Preconditions.checkArgument(!CuratorFrameworkState.STOPPED.equals(client.getState()),
      "Curator客户端已停止!");
    Preconditions.checkArgument(StringUtils.isNotBlank(membershipPath), " 使用的ZNode路径不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(memberId), "成员标识不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(memberDescription), " 成员描述不可为空!");

    this.client = client;
    this.zookeeperConnectionString = client.getZookeeperClient().getCurrentConnectionString();
    this.membershipPath = membershipPath;
    this.memberId = memberId;
    this.memberDescription = memberDescription;

    this.init();
  }

  /**
   * 加入群组.
   */
  public void join() {
    LOG.info("Member[{}: {}] join Group[{}] on Quorum[{}]", memberId, memberDescription,
      membershipPath, zookeeperConnectionString);

    groupMember.start();
  }

  /**
   * 重新加入群组.
   */
  public void rejoin() {
    LOG.info("Member[{}: {}] rejoin Group[{}] on Quorum[{}]", memberId, memberDescription,
      membershipPath, zookeeperConnectionString);

    this.init();
    groupMember.start();
  }

  /**
   * 退出群组.
   * @param groupMember
   */
  public void quit() {
    LOG.info("Member[{}: {}] quit Group[{}] on Quorum[{}]", memberId, memberDescription,
      membershipPath, zookeeperConnectionString);

    groupMember.close();
  }

  // 初始化GroupMember.
  private void init() {
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

    // 组成员
    groupMember = new GroupMember(client, membershipPath, memberId, memberDescription.getBytes());
  }

  /**
   * 获取组中所有成员.
   * @param groupMember
   * @return Map[memberId, memberDescription]
   */
  public Map<String, String> getMembers() {
    Map<String, String> result = Maps.newHashMap();

    Map<String, byte[]> members = groupMember.getCurrentMembers();
    if (MapUtils.isNotEmpty(members)) {
      for (String memberId : members.keySet()) {
        result.put(memberId, String.valueOf(members.get(memberId)));
      }
    }

    return result;
  }

}
