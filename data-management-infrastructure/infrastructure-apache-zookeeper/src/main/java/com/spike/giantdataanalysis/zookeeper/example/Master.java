package com.spike.giantdataanalysis.zookeeper.example;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.zookeeper.support.ZooKeepers;

public class Master {
  private static final Logger LOG = LoggerFactory.getLogger(Master.class);

  // the Zookeeper handle
  private ZooKeeper zk;

  // the id of server that try to compete for mastership
  private String serverId = Integer.toHexString(new Random().nextInt());

  /**
   * master's znode path
   */
  public static final String MASTER_ZNODE_PATH = "/master";

  // flag of gain the mastership
  private static boolean isLeader = false;

  public static void main(String[] args) throws Exception {
    Master master = new Master();
    master.startZK();

    master.runForMaster();

    if (isLeader) {
      LOG.info("I'm the leader");

      // wait for a bit: 1 minute
      Thread.sleep(60000L);

    } else {

      LOG.info("Someone else is the leader");
    }

    master.stopZK();
  }

  private static class MasterWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
      // just render the event information
      LOG.info("watched event: " + event);
    }

    @Override
    public String toString() {
      return "<" + this.getClass().getName() + ">";
    }
  }

  /**
   * construct Zookeeper handle bindings
   */
  public void startZK() {
    try {
      String connectString = ZooKeepers.DEFAULT_QUORUM_CONN_STRING;
      int sessionTimeout = 15000;
      Watcher watcher = new MasterWatcher();

      LOG.info("establish a session handle with settings: [connectString=" + connectString
          + ", sessionTimeout=" + sessionTimeout + ",watcher=" + watcher + "]");

      zk = new ZooKeeper(connectString, sessionTimeout, watcher);

    } catch (IOException e) {
      LOG.error("startZK failed", e);
    }
  }

  /**
   * compete for mastership
   * @throws InterruptedException
   */
  public void runForMaster() throws InterruptedException {
    while (true) {
      try {
        LOG.info("create path with parameters: " + MASTER_ZNODE_PATH + "," + serverId + ","
            + ZooDefs.Ids.OPEN_ACL_UNSAFE + "," + CreateMode.EPHEMERAL);

        zk.create(//
          MASTER_ZNODE_PATH,//
          serverId.getBytes(), //
          ZooDefs.Ids.OPEN_ACL_UNSAFE, //
          CreateMode.EPHEMERAL);

      } catch (InterruptedException e) {
        throw e;
      } catch (KeeperException e) {
        // exception type check
        if (e instanceof NodeExistsException) {// failed
          isLeader = false;
          break;
        } else if (e instanceof ConnectionLossException) {
          // do nothing
        }
      }

      // running check status
      if (this.checkMaster()) {// success
        break;
      }

    }
  }

  /**
   * check whether any one server has gained the mastership already
   * @return
   * @throws InterruptedException
   */
  private boolean checkMaster() throws InterruptedException {
    while (true) {
      try {

        Stat stat = new Stat();
        // just want to know current data, not setting the watch
        byte[] data = zk.getData(MASTER_ZNODE_PATH, false, stat);

        // check whether current server thread gain the mastership
        isLeader = new String(data).equals(serverId);

        return true;

      } catch (InterruptedException e) {
        throw e;
      } catch (KeeperException e) {
        if (e instanceof NoNodeException) {
          // no master, can pursue for master later
          return false;
        } else if (e instanceof ConnectionLossException) {
          // do nothing
        }
      }
    }
  }

  /**
   * close the Zookeeper handle
   */
  public void stopZK() {
    try {
      LOG.info("try to close the session");

      zk.close();

    } catch (InterruptedException e) {
      LOG.error("stopZK failed", e);
    }
  }

  @Override
  public String toString() {
    return "<" + this.getClass().getName() + ">";
  }
}
