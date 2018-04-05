package com.spike.giantdataanalysis.coordination.lock;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.spike.giantdataanalysis.coordination.Coordinations;
import com.spike.giantdataanalysis.coordination.exception.CoordinationException;

/**
 * 锁协同.
 * @author zhoujiagen
 */
public class LockCoordination {
  private static final Logger LOG = LoggerFactory.getLogger(LockCoordination.class);

  private final String zookeeperConnectionString;
  private final String lockPath;
  private final String acquirer;

  private CuratorFramework client;
  private InterProcessMutex curatorLock;

  /**
   * 锁协同.
   * @param zookeeperConnectionString ZK连接串
   * @param lockPath 使用的锁ZNode路径
   * @param acquirer 获取者标识
   */
  public LockCoordination(String zookeeperConnectionString, String lockPath, String acquirer) {

    Preconditions.checkArgument(StringUtils.isNotBlank(zookeeperConnectionString), "ZK连接串不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(lockPath), " 使用的锁ZNode路径不可为空!");
    Preconditions.checkArgument(StringUtils.isNotBlank(acquirer), "获取者标识不可为空!");

    this.zookeeperConnectionString = zookeeperConnectionString;
    this.lockPath = lockPath;
    this.acquirer = acquirer;

    this.init();
  }

  private void init() {
    // 客户端
    client =
        CuratorFrameworkFactory.newClient(zookeeperConnectionString,
          Coordinations.DEFAULT_CURATOR_RETRY_POLICY);
    client.start();

    curatorLock = new InterProcessMutex(client, lockPath);
  }

  /** 阻塞获取锁 */
  public void acquire() throws CoordinationException {
    LOG.info("Acquirer[{}] try acquire lock[{}] on Quorum[{}]", acquirer, lockPath,
      zookeeperConnectionString);

    try {

      curatorLock.acquire();
      LOG.info("Acquirer[{}] successfully acquire lock[{}] on Quorum[{}]", acquirer, lockPath,
        zookeeperConnectionString);

    } catch (Exception e) {

      LOG.info("Acquirer[{}] failed to acquire lock[{}] on Quorum[{}]", acquirer, lockPath,
        zookeeperConnectionString);
      throw CoordinationException.newException(e);
    }
  }

  /** 带超时获取锁 */
  public boolean acquire(long time, TimeUnit unit) throws CoordinationException {
    LOG.info("Acquirer[{}] try acquire lock[{}] on Quorum[{}] with timeout[{} {}]", acquirer,
      lockPath, zookeeperConnectionString, time, unit);

    try {

      boolean result = curatorLock.acquire(time, unit);
      LOG.info("Acquirer[{}] successfully acquire lock[{}] on Quorum[{}] with timeout[{} {}]",
        acquirer, lockPath, zookeeperConnectionString, time, unit);
      return result;

    } catch (Exception e) {

      LOG.info("Acquirer[{}] failed to acquire lock[{}] on Quorum[{}] with timeout[{} {}]",
        acquirer, lockPath, zookeeperConnectionString, time, unit);
      throw CoordinationException.newException(e);
    }
  }

  /** 释放锁 */
  public void release() {
    LOG.info("Acquirer[{}] try release lock[{}] on Quorum[{}]", acquirer, lockPath,
      zookeeperConnectionString);

    try {

      curatorLock.release();
      LOG.info("Acquirer[{}] successfully release lock[{}] on Quorum[{}]", acquirer, lockPath,
        zookeeperConnectionString);

    } catch (Exception e) {

      LOG.info("Acquirer[{}] failed to release lock[{}] on Quorum[{}]", acquirer, lockPath,
        zookeeperConnectionString);
      throw CoordinationException.newException(e);
    }
  }

}
