package com.spike.giantdataanalysis.procedure.mw;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhoujiagen@gmail.com
 */
public class MasterWorkerConstants {

  private static final Logger LOG = LoggerFactory.getLogger(MasterWorkerConstants.class);

  /** 根路径 */
  public static String ROOT_PATH = "/";

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

}
