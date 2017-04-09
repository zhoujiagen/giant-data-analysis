package com.spike.giantdataanalysis.zookeeper.example;

import java.util.Date;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.zookeeper.support.ZooKeepers;

/**
 * 管理客户端
 * @author zhoujiagen
 */
public class AdminClient {
  private static final Logger LOG = LoggerFactory.getLogger(AdminClient.class);

  // the Zookeeper handle
  private ZooKeeper zk;

  public static void main(String[] args) throws Exception {
    AdminClient ac = new AdminClient();

    ac.start();

    ac.listState();
  }

  private static final class AdminClientWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
      LOG.info("watched event: " + event);
    }
  }

  /**
   * construct the Zookeeper handle
   * @throws Exception
   */
  public void start() throws Exception {
    zk = new ZooKeeper(ZooKeepers.DEFAULT_CONN_STRING, 15000, new AdminClientWatcher());
  }

  /**
   * list status of the system record in Zookeeper service
   */
  public void listState() {
    Stat stat = new Stat();
    try {
      byte[] masterData = zk.getData(//
        Master.MASTER_ZNODE_PATH, //
        false, //
        stat//
          );

      Date startDate = new Date(stat.getCtime());

      LOG.info("Master: " + new String(masterData) + " since" + startDate);

    } catch (KeeperException | InterruptedException e) {
      if (e instanceof NoNodeException) {
        LOG.info("no master");
      }
    }

    try {
      LOG.info("Workers: ");
      for (String worker : zk.getChildren("/workers", false)) {
        byte[] data = zk.getData(//
          Worker.WORKER_PARENT_ZNODE_PATH + worker, //
          false, //
          null//
            );
        String status = new String(data);
        LOG.info("\t" + worker + ": " + status);
      }

      LOG.info("Tasks:");
      for (String task : zk.getChildren("/assign", false)) {
        LOG.info("\t" + task);
      }
    } catch (KeeperException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
