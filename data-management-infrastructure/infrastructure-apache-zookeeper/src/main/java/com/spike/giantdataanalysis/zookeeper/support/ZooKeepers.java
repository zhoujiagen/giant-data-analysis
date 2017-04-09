package com.spike.giantdataanalysis.zookeeper.support;

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
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ZooKeepers {
  private static final Logger LOG = LoggerFactory.getLogger(ZooKeepers.class);

  private static final File QUORUM_WORK_DIR =
      new File("src/main/resources/zookeeper-3nodes-quorum");

  /** 根路径 */
  public static String ROOT_PATH = "/";
  private static String NEWLINE = System.lineSeparator();

  public static void main(String[] args) throws Exception {
    START_QUORUM();
    // STOP_QUORUM();
  }

  // ======================================== properties

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

  // znode paths
  public static final String MASTER_ZNODE_PATH = "/master";

  public static final String WORKER_PARENT_ZNODE_PATH = "/workers";
  public static final String WORKER_PARENT_ZNODE_PATH_PREFIX = "/workers/";
  public static final String WORKER_ZNODE_PATH_PREFIX = "/workers/worker-";

  public static final String TASK_PARENT_ZNODE_PATH = "/tasks";
  public static final String TASK_PARENT_ZNODE_PATH_PREFIX = "/tasks/";
  public static final String TASK_ZNODE_PATH_PREFIX = "/tasks/task-";

  public static final String STATUS_PARENT_ZNODE_PATH = "/status";
  public static final String STATUS_PARENT_ZNODE_PATH_PREFIX = "/status/";
  public static final String STATUS_TASK_ZNODE_PATH_PREFIX = "/status/task-";

  public static final String ASSIGN_PARENT_ZNODE_PATH = "/assign";
  public static final String ASSIGN_PARENT_ZNODE_PATH_PREFIX = "/assign/";
  public static final String ASSIGN_WORKER_ZNONE_PATH_PREFIX = "/assign/worker-";

  // ======================================== methods

  /**
   * 启动三节点集群
   * @throws Exception
   */
  public static void START_QUORUM() throws Exception {
    Process process =
        Runtime.getRuntime().exec(new String[] { "./start_all.sh" }, new String[] {},
          QUORUM_WORK_DIR);

    process.waitFor();

    System.out.println(process.exitValue());
  }

  /**
   * 停止三节点集群
   * @throws Exception
   */
  public static void STOP_QUORUM() throws Exception {
    Process process =
        Runtime.getRuntime().exec(new String[] { "./kill_all.sh" }, new String[] {},
          QUORUM_WORK_DIR);

    process.waitFor();

    System.out.println(process.exitValue());
  }

  /**
   * 展示目录树
   * @param connectionString ZooKeeper连接串
   * @param root 根目录
   * @param depth 深度
   */
  public static void listTree(String connectionString, String root, int depth) {
    Preconditions.checkNotNull(connectionString);
    Preconditions.checkNotNull(root);
    Preconditions.checkState(depth > 0);

    if (!root.startsWith(ROOT_PATH)) {
      root = ROOT_PATH + root;
    }

    StringBuilder sb = new StringBuilder();
    sb.append(root + NEWLINE);

    try {
      // 1 init handle
      ZooKeeper zooKeeper = new ZooKeeper(connectionString, 15000, DEFAULT_WATCHER);

      // 2 do process
      List<String> rootChildren = zooKeeper.getChildren(root, false);
      if (CollectionUtils.isNotEmpty(rootChildren)) {
        for (String rootChild : rootChildren) {
          sb.append(doListTree(zooKeeper, preAppendPath(root, rootChild), depth - 1));
        }
      }

      // System.out.println(sb.toString());

      String[] records = sb.toString().split(NEWLINE);

      List<String> recordList = Arrays.asList(records);
      Collections.sort(recordList);
      for (String record : recordList) {
        System.out.println(record);
      }

      // 3 close handle
      zooKeeper.close();

    } catch (Exception e) {
      LOG.error("查看树目录结构失败", e);
    }
  }

  private static String doListTree(ZooKeeper zooKeeper, String root, int depth)
      throws KeeperException, InterruptedException {
    StringBuilder sb = new StringBuilder();

    if (depth == 0) {

      sb.append(root + NEWLINE);

    } else {

      List<String> rootChildren = zooKeeper.getChildren(root, false);
      if (CollectionUtils.isNotEmpty(rootChildren)) {
        sb.append(root + NEWLINE);
        for (String rootChild : rootChildren) {
          sb.append(doListTree(zooKeeper, preAppendPath(root, rootChild), depth - 1));
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

  // ======================================== classes

}
