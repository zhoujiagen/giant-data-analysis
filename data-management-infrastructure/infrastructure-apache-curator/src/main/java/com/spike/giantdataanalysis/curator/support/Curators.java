package com.spike.giantdataanalysis.curator.support;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Curators {
  private static final Logger LOG = LoggerFactory.getLogger(Curators.class);

  // ======================================== properties

  // ======================================== methods

  /**
   * 创建客户端
   * @param connectString ZooKeeper连接串
   * @param retryPolicy 重试策略
   * @return
   * @see CuratorFramework#start()
   * @see CuratorFramework#close()
   */
  public static CuratorFramework CLIENT(String connectString, RetryPolicy retryPolicy) {
    CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    return client;
  }

  /**
   * 指数回退重试策略
   * @param baseSleepTimeMs 第一次重试的等待时间
   * @param maxRetries 最大重试次数
   * @return
   */
  public static RetryPolicy EXPONENTIAL_BACKOFF_RETRY(int baseSleepTimeMs, int maxRetries) {
    return new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
  }

  /**
   * 指数回退重试策略
   * @param baseSleepTimeMs 第一次重试的等待时间
   * @param maxRetries 最大重试次数
   * @param maxSleepMs 每次重试睡眠最大时间
   * @return
   */
  public static RetryPolicy EXPONENTIAL_BACKOFF_RETRY(int baseSleepTimeMs, int maxRetries,
      int maxSleepMs) {
    return new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries, maxSleepMs);
  }

  // ======================================== classes

  public static final class ZooKeepers {

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
    public static final CreateMode NODE_MODE_PERSISTENT_SEQUENTIAL =
        CreateMode.PERSISTENT_SEQUENTIAL;
    public static final CreateMode NODE_MODE_EPHEMERAL_SEQUENTIAL = CreateMode.EPHEMERAL_SEQUENTIAL;

    // watcher
    public static final Watcher DEFAULT_WATCHER = new Watcher() {
      @Override
      public void process(WatchedEvent event) {
        LOG.info("default watcher watched event: " + event);
      }
    };

    // ======================================== methods

    // ======================================== classes
  }
}
