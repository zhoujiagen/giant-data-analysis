package com.spike.giantdataanalysis.zookeeper.example;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.zookeeper.support.ZooKeepers;

public class Worker {
  private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

  // the Zookeeper handle
  private ZooKeeper zk;

  // the worker server identifier
  private String serverId = Integer.toHexString(new Random().nextInt());

  // status of worker server
  private String status;

  /**
   * znode path of workers
   */
  public static final String WORKER_PARENT_ZNODE_PATH = "/workers";

  /**
   * prefix of worker's znode
   */
  public static final String WORKER_ZNODE_PATH_PREFIX = WORKER_PARENT_ZNODE_PATH + "/worker-";

  public static void main(String[] args) throws Exception {
    Worker worker = new Worker();

    worker.startZK();

    worker.register();

    // wait a bit
    Thread.sleep(30000L);
  }

  private static final class WorkerWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
      LOG.info("watched event: " + event);
    }
  }

  /**
   * construct Zookeeper handle
   * @throws IOException
   */
  public void startZK() throws IOException {
    zk = new ZooKeeper(ZooKeepers.DEFAULT_CONN_STRING, 15000, new WorkerWatcher());
  }

  private final StringCallback createWorkerCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (Code.get(rc)) {
      case CONNECTIONLOSS:
        register();
        break;
      case OK:
        LOG.info("registered successfully: " + serverId);
        break;
      case NODEEXISTS:
        LOG.error("already registered: " + serverId);
        break;
      default:
        LOG.error("something wrong:" + KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * register worker to system
   */
  public void register() {

    // set worker's status to "Idle"
    zk.create(//
      WORKER_ZNODE_PATH_PREFIX + serverId,//
      "Idle".getBytes(), //
      ZooDefs.Ids.OPEN_ACL_UNSAFE, //
      CreateMode.EPHEMERAL,//
      createWorkerCallback, //
      null//
    );
  }

  /**
   * update worker's status
   * @param status
   */
  public void setStatus(String status) {
    this.status = status;

    this.updateStatus(status);
  }

  private final StatCallback statusUpdateCallback = new StatCallback() {

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
      switch (Code.get(rc)) {
      case CONNECTIONLOSS:
        updateStatus((String) ctx);
        return;

      default:
        break;
      }
    }
  };

  /**
   * change worker's status in synchronize mode
   * @param status
   */
  private synchronized void updateStatus(String status) {

    if (status.equals(this.status)) {// re-check
      zk.setData(//
        WORKER_ZNODE_PATH_PREFIX + serverId, //
        status.getBytes(),//
        -1, // for all version
        statusUpdateCallback, //
        status// the context object
      );
    }
  }

}
