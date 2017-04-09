package com.spike.giantdataanalysis.zookeeper.example;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.zookeeper.support.ZooKeepers;

public class Client {
  private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

  // the Zookeeper handle
  private ZooKeeper zk;

  /**
   * prefix of tasks' znode path
   */
  public static final String TASK_ZNODE_PATH_PREFIX = "/tasks/task-";

  public static void main(String[] args) throws Exception {
    Client client = new Client();

    client.startZK();

    String name = client.queueCommand("cmd");
    LOG.info(name + "created");

  }

  private static class ClientWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {
      LOG.info("watched event: " + event);
    }
  }

  /**
   * construct the Zookeeper handle
   * @throws IOException
   */
  public void startZK() throws IOException {
    zk = new ZooKeeper(ZooKeepers.DEFAULT_CONN_STRING, 15000, new ClientWatcher());
  }

  /**
   * create tasks with command
   * @param command the work should done by the system(worker)
   * @return
   * @throws Exception
   */
  public String queueCommand(String command) throws Exception {
    while (true) {
      String name = null;

      try {
        name = zk.create(//
          TASK_ZNODE_PATH_PREFIX, //
          command.getBytes(), //
          ZooKeepers.DEFAULT_ACLS,//
          CreateMode.PERSISTENT_SEQUENTIAL//
            );

        return name;

      } catch (KeeperException | InterruptedException e) {

        if (e instanceof NodeExistsException) {
          throw new Exception(name + " already appears to running");
        }
      }
    }
  }
}
