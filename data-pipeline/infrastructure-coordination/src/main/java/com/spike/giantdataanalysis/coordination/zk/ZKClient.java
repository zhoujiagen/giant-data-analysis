package com.spike.giantdataanalysis.coordination.zk;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.OpResult;
import org.apache.zookeeper.Transaction;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * ZK客户端抽象.
 * @author zhoujiagen@gmail.com
 */
public class ZKClient {
  private static final Logger LOG = LoggerFactory.getLogger(ZKClient.class);

  // ---------------------------------------------------------------------------
  // properties
  // ---------------------------------------------------------------------------

  private ZooKeeper zooKeeper;
  private volatile boolean connected = false;
  private volatile boolean sessionExpired = false;
  private List<ACL> acls = Lists.newArrayList();
  /** 客户端中创建的节点. */
  private Map<String, CreateMode> nodeMap = Maps.newConcurrentMap();

  // ---------------------------------------------------------------------------
  // methods
  // ---------------------------------------------------------------------------

  public ZKClient(String connectString, int sessionTimeout) throws IOException {
    zooKeeper =
        new ZooKeeper(connectString, sessionTimeout, new DefaultZKClientConnectionWatcher());
    acls.addAll(ZooDefs.Ids.OPEN_ACL_UNSAFE);
  }

  /**
   * 注册Watcher.
   * @param watcher
   */
  public void registerWatcher(Watcher watcher) {
    zooKeeper.register(watcher);
  }

  public ZKClient(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
    zooKeeper = new ZooKeeper(connectString, sessionTimeout, watcher);
    acls.addAll(ZooDefs.Ids.OPEN_ACL_UNSAFE);
  }

  public boolean isConnected() {
    return connected;
  }

  public boolean isSessionExpired() {
    return sessionExpired;
  }

  public void initialize() {
    while (!isConnected() && !isSessionExpired()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Wait for connection...");
      }
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        // ignore
      }
    }

    if (!isConnected()) {
      throw new RuntimeException("initialize failed!");
    } else {
      LOG.info("Connection established!");
    }
  }

  public void close() throws InterruptedException {
    if (zooKeeper != null) {
      LOG.info("Close client");
      zooKeeper.close();
    }
  }

  /**
   * 创建节点.
   * @param path
   * @param data
   * @param createMode
   * @throws InterruptedException
   * @throws KeeperException
   */
  public void create(final String path, byte data[], CreateMode createMode)
      throws KeeperException, InterruptedException {
    Preconditions.checkArgument(StringUtils.isNotBlank(path));
    Preconditions.checkArgument(createMode != null);

    zooKeeper.create(path, data, acls, createMode);

    // 更新内存节点记录
    nodeMap.put(path, createMode);
  }

  /**
   * 删除节点.
   * @param path
   * @param version
   * @throws KeeperException
   * @throws InterruptedException
   */
  public void delete(final String path, int version) throws InterruptedException, KeeperException {
    Preconditions.checkArgument(StringUtils.isNotBlank(path));

    try {
      zooKeeper.delete(path, version);
    } catch (InterruptedException e) {
      throw e;
    } catch (KeeperException e) {
      throw e;
    }

    nodeMap.remove(path);
  }

  /**
   * 检查节点是否存在.
   * @param path
   * @throws KeeperException
   * @throws InterruptedException
   */
  public Stat exists(final String path) throws KeeperException, InterruptedException {
    Preconditions.checkArgument(StringUtils.isNotBlank(path));

    return zooKeeper.exists(path, false);
  }

  /**
   * 获取子节点.
   * @param path
   * @return
   * @throws KeeperException
   * @throws InterruptedException
   */
  public List<String> getChildren(final String path, boolean watch)
      throws KeeperException, InterruptedException {
    Preconditions.checkArgument(StringUtils.isNotBlank(path));

    return zooKeeper.getChildren(path, watch);
  }

  /**
   * 获取节点数据.
   * @param path
   * @param stat
   * @return
   * @throws KeeperException
   * @throws InterruptedException
   */
  public byte[] getData(final String path, Stat stat) throws KeeperException, InterruptedException {
    Preconditions.checkArgument(StringUtils.isNotBlank(path));

    return zooKeeper.getData(path, false, stat);
  }

  /**
   * 设置节点数据
   * @param path
   * @param data
   * @param version
   * @return
   * @throws KeeperException
   * @throws InterruptedException
   */
  public Stat setData(final String path, byte data[], int version)
      throws KeeperException, InterruptedException {
    Preconditions.checkArgument(StringUtils.isNotBlank(path));

    return zooKeeper.setData(path, data, version);
  }

  /**
   * 多个操作.
   * @param ops
   * @return
   * @throws InterruptedException
   * @throws KeeperException
   */
  public List<OpResult> multi(Iterable<Op> ops) throws InterruptedException, KeeperException {
    Preconditions.checkArgument(ops != null && ops.iterator().hasNext());

    return zooKeeper.multi(ops);
  }

  /**
   * 同步节点.
   * @param path
   * @param cb
   * @param ctx
   */
  public void sync(final String path, VoidCallback cb, Object ctx) {
    Preconditions.checkArgument(StringUtils.isNotBlank(path));

    zooKeeper.sync(path, cb, ctx);
  }

  /**
   * 获取事务句柄.
   * @return
   */
  public Transaction tx() {
    return zooKeeper.transaction();
  }

  // ---------------------------------------------------------------------------
  // classes: Watcher
  // ---------------------------------------------------------------------------

  /**
   * 默认的客户端连接Watcher.
   */
  private class DefaultZKClientConnectionWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Got watched event: " + event);
      }

      // handle events except:
      // NodeCreated, NodeDeleted, NodeDataChanged, NodeChildrenChanged
      if (Event.EventType.None.equals(event.getType())) {
        switch (event.getState()) {
        case SyncConnected:
          connected = true;
          LOG.info("Session connected!");
          break;

        case Disconnected:
          connected = false;
          LOG.warn("Connection disconnected!");
          break;

        case Expired:
          sessionExpired = true;
          connected = false;
          LOG.warn("Session expired!");
          break;

        default:
          LOG.warn("WatchedEvent unhandled, event={}", event);
          break;
        }
      }

    }

  }
}
