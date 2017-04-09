package com.spike.giantdataanalysis.zookeeper.example;

import java.io.IOException;
import java.util.Random;

import org.apache.zookeeper.AsyncCallback.DataCallback;
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

public class MasterUsingAsync {
  private static final Logger LOG = LoggerFactory.getLogger(MasterUsingAsync.class);

  // the Zookeeper handle
  private ZooKeeper zk;

  // the id of server that try to compete for mastership
  String serverId = Integer.toHexString(new Random().nextInt());

  /**
   * master's znode path
   */
  public static final String MASTER_ZNODE_PATH = "/master";

  // flag of gain the mastership
  private static boolean isLeader = false;

  private static class MasterWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
      LOG.info("watched event: " + event);
    }

    @Override
    public String toString() {
      return "<" + this.getClass().getName() + ">";
    }
  }

  public static void main(String[] args) throws Exception {
    MasterUsingAsync master = new MasterUsingAsync();
    // master.startZK();

    // currently placed here
    // or put in bootstrap in application
    master.bootstrap();

    // master.runForMaster();

    // wait for a bit, 1 minute
    Thread.sleep(60000L);

    master.stopZK();
  }

  @Override
  public String toString() {
    return "MasterUsingAsync [serverId=" + serverId + "]";
  }

  /**
   * check whether any one server has gained the mastership already
   */
  private void checkMaster() {
    DataCallback masterCheckCallback = new DataCallback() {
      @Override
      public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        LOG.info(this + " process result with parameters: [rc=" + rc + ",path=" + path + ",ctx="
            + ctx + ",data=" + new String(data) + ",stat=" + stat + "]");

        switch (KeeperException.Code.get(rc)) {
        case CONNECTIONLOSS:
          checkMaster();// check again
          return;

        case NONODE:
          runForMaster();// compete for mastership
          return;

        default:
          break;
        }
      }

      @Override
      public String toString() {
        return "<" + this.getClass().getName() + ">";
      }
    };

    zk.getData(//
      MASTER_ZNODE_PATH, //
      false, //
      masterCheckCallback, //
      null// the context object
    );
  }

  private void runForMaster() {
    LOG.info(this + " run for mater");

    StringCallback masterCreateCallback = new StringCallback() {
      @Override
      public void processResult(int rc, String path, Object ctx, String name) {
        LOG.info(this + " process result with parameters: [rc=" + rc + ",path=" + path + ",ctx="
            + ctx + ",name=" + name + "]");

        switch (KeeperException.Code.get(rc)) {
        case CONNECTIONLOSS:
          checkMaster();// check mastership
          return;

        case OK:
          isLeader = true;
          break;

        default:
          isLeader = false;
        }
        LOG.info("I am leader? " + isLeader);
      }

      @Override
      public String toString() {
        return "<" + this.getClass().getName() + ">";
      }
    };

    LOG.info(this + " create path with parameters: " + MASTER_ZNODE_PATH + "," + serverId + ","
        + ZooDefs.Ids.OPEN_ACL_UNSAFE + "," + CreateMode.EPHEMERAL + "," + masterCreateCallback
        + ", null");

    zk.create(//
      MASTER_ZNODE_PATH, //
      serverId.getBytes(), // the real data
      ZooKeepers.DEFAULT_ACLS, //
      CreateMode.EPHEMERAL,//
      masterCreateCallback, //
      null// the context object
    );
  }

  /**
   * construct Zookeeper handle bindings
   */
  private void startZK() {
    try {
      String connectString = ZooKeepers.DEFAULT_CONN_STRING;
      int sessionTimeout = 15000;
      Watcher watcher = new MasterWatcher();

      LOG.info(this + " establish a session handle with settings: [connectString=" + connectString
          + ", sessionTimeout=" + sessionTimeout + ",watcher=" + watcher + "]");

      zk = new ZooKeeper(connectString, sessionTimeout, watcher);

    } catch (IOException e) {
      LOG.error(this + " startZK failed", e);
    }

  }

  public void stopZK() {
    try {
      LOG.info(this + " try to close the session");

      zk.close();

    } catch (InterruptedException e) {
      LOG.error(this + " stopZK failed", e);
    }
  }

  public void bootstrap() {
    LOG.info(this + " try to bootstrap");

    this.startZK();

    this.createZnode("/workers", new byte[0]);
    this.createZnode("/assign", new byte[0]);
    this.createZnode("/tasks", new byte[0]);
    this.createZnode("/status", new byte[0]);

    this.runForMaster();
  }

  /**
   * create znode
   * @param path the znode path
   * @param data the znode data
   */
  private void createZnode(String path, byte[] data) {
    LOG.info(this + " try to create metadata with parameters: path=" + path + ",data="
        + new String(data));

    StringCallback createMetadataCallback = new StringCallback() {
      @Override
      public void processResult(int rc, String path, Object ctx, String name) {
        LOG.info(this + " process result with parameters: [rc=" + rc + ",path=" + path + ",ctx="
            + ctx + ",name=" + name + "]");

        switch (KeeperException.Code.get(rc)) {
        case CONNECTIONLOSS:
          createZnode(path, (byte[]) ctx);// recreate
          break;

        case OK:
          LOG.info(path + " created.");
          break;

        case NODEEXISTS:
          LOG.info(path + " already existed!!! ");
          break;

        default:
          LOG.info("something wrong: " + KeeperException.create(Code.get(rc), path));
        }
      }

      @Override
      public String toString() {
        return "<" + this.getClass().getName() + ">";
      }
    };

    zk.create(//
      path, //
      data, // real data
      ZooDefs.Ids.OPEN_ACL_UNSAFE, //
      CreateMode.PERSISTENT, //
      createMetadataCallback, //
      data// the context object
    );
  }
}
