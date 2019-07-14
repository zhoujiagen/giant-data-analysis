package com.spike.giantdataanalysis.coordination.zk;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * ZK工具类.
 * @author zhoujiagen@gmail.com
 */
public class ZKTool {
  private static final Logger LOG = LoggerFactory.getLogger(ZKTool.class);

  private static final File QUORUM_WORK_DIR =
      new File("src/main/resources/zookeeper-3nodes-quorum");

  /** 根路径 */
  public static String ROOT_PATH = "/";
  private static String NEWLINE = System.lineSeparator();

  // connection strings
  public static final String DEFAULT_CONN_STRING = "127.0.0.1:2181";
  public static final String DEFAULT_QUORUM_CONN_STRING =
      "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

  // acls
  public static final List<ACL> DEFAULT_ACLS = ZooDefs.Ids.OPEN_ACL_UNSAFE;

  // znode modes
  public static final CreateMode NODE_MODE_PERSISTENT = CreateMode.PERSISTENT;
  public static final CreateMode NODE_MODE_EPHEMERAL = CreateMode.EPHEMERAL;
  public static final CreateMode NODE_MODE_PERSISTENT_SEQUENTIAL = CreateMode.PERSISTENT_SEQUENTIAL;
  public static final CreateMode NODE_MODE_EPHEMERAL_SEQUENTIAL = CreateMode.EPHEMERAL_SEQUENTIAL;

  // watcher
  public static final Watcher DEFAULT_WATCHER = new Watcher() {
    @Override
    public void process(WatchedEvent event) {
      LOG.info("default watcher watched event: " + event);
    }
  };

  /**
   * 启动本地三节点集群.
   * @throws Exception
   */
  public static void startLocalQuorum() throws Exception {
    Process process = Runtime.getRuntime().exec(//
      new String[] { "./start_all.sh" }, //
      new String[] {}, //
      QUORUM_WORK_DIR);

    process.waitFor();

    LOG.info("Process exit value = {}", process.exitValue());
  }

  /**
   * 停止本地三节点集群.
   * @throws Exception
   */
  public static void stopLocalQuorum() throws Exception {
    Process process = Runtime.getRuntime().exec(//
      new String[] { "./kill_all.sh" }, //
      new String[] {}, //
      QUORUM_WORK_DIR);

    process.waitFor();

    LOG.info("Process exit value = {}", process.exitValue());
  }

  /**
   * 展示目录树
   * @param zkClient ZooKeeper客户端
   * @param root 根目录
   * @param depth 深度
   */
  public static void listTree(ZKClient zkClient, String root, int depth) {
    Preconditions.checkNotNull(zkClient);
    Preconditions.checkNotNull(root);
    Preconditions.checkState(depth > 0);

    if (!root.startsWith(ROOT_PATH)) {
      root = ROOT_PATH + root;
    }

    StringBuilder sb = new StringBuilder();
    sb.append(root + NEWLINE);

    try {

      // 2 do process
      List<String> rootChildren = zkClient.getChildren(root, false);
      if (CollectionUtils.isNotEmpty(rootChildren)) {
        for (String rootChild : rootChildren) {
          sb.append(doListTree(zkClient, preAppendPath(root, rootChild), depth - 1));
        }
      }

      String[] records = sb.toString().split(NEWLINE);

      List<String> recordList = Arrays.asList(records);
      Collections.sort(recordList);
      for (String record : recordList) {
        System.out.println(record);
      }

    } catch (Exception e) {
      LOG.error("查看树目录结构失败", e);
    }
  }

  private static String doListTree(ZKClient zkClient, String root, int depth)
      throws KeeperException, InterruptedException {
    StringBuilder sb = new StringBuilder();

    if (depth == 0) {

      sb.append(root + NEWLINE);

    } else {

      List<String> rootChildren = zkClient.getChildren(root, false);
      if (CollectionUtils.isNotEmpty(rootChildren)) {
        sb.append(root + NEWLINE);
        for (String rootChild : rootChildren) {
          sb.append(doListTree(zkClient, preAppendPath(root, rootChild), depth - 1));
        }
      } else {
        sb.append(root + NEWLINE);
      }
    }

    return sb.toString();
  }

  private static String preAppendPath(String root, String child) {
    if (StringUtils.isBlank(root) || StringUtils.isBlank(child)) {
      return null;
    }

    if (ROOT_PATH.equals(root)) {
      return root + child;
    } else {
      return root + ROOT_PATH + child;
    }

  }

}
