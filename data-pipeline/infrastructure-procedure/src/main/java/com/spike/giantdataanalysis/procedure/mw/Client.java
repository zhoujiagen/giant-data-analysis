package com.spike.giantdataanalysis.procedure.mw;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.procedure.mw.MasterWorkerConstants;

/**
 * <pre>
 * The Client implementation
 * 
 * should watch: 
 * 1 task execution result
 * </pre>
 * 
 * @author zhoujiagen
 */
public class Client {
  private static final Logger LOG = LoggerFactory.getLogger(Client.class);

  // the Zookeeper handle
  private ZooKeeper zk;

  // the ZooKeeper Service connection
  private String hostAndPort;

  // flags about connection and session
  private volatile boolean connected = false;
  private volatile boolean sessionExpired = false;

  protected ConcurrentHashMap<String, Object> ctxMap = new ConcurrentHashMap<String, Object>();

  public Client(String hostPort) {
    this.hostAndPort = hostPort;
  }

  static class TaskObject {
    private String task;
    private String taskName;
    private boolean done = false;
    private boolean succesful = false;
    private CountDownLatch latch = new CountDownLatch(1);

    String getTask() {
      return task;
    }

    void setTask(String task) {
      this.task = task;
    }

    void setTaskName(String name) {
      this.taskName = name;
    }

    String getTaskName() {
      return taskName;
    }

    void setStatus(boolean status) {
      succesful = status;
      done = true;
      latch.countDown();
    }

    void waitUntilDone() {
      try {
        latch.await();
      } catch (InterruptedException e) {
        LOG.warn("InterruptedException while waiting for task to get done");
      }
    }

    synchronized boolean isDone() {
      return done;
    }

    synchronized boolean isSuccesful() {
      return succesful;
    }

  }

  public static void main(String args[]) throws Exception {
    Client client = new Client(MasterWorkerConstants.DEFAULT_CONN_STRING);
    client.startZK();

    while (!client.isConnected()) {
      Thread.sleep(100);
    }

    TaskObject task1 = new TaskObject();
    TaskObject task2 = new TaskObject();

    client.submitTask("Sample task", task1);
    client.submitTask("Another sample task", task2);

    task1.waitUntilDone();
    task2.waitUntilDone();
  }

  // client's watcher, the default watcher
  private Watcher clientWatcher = new Watcher() {
    @Override
    public void process(WatchedEvent event) {
      LOG.info("watched event: " + event);

      // handle events except:
      // NodeCreated, NodeDeleted, NodeDataChanged, NodeChildrenChanged
      if (Event.EventType.None.equals(event.getType())) {
        switch (event.getState()) {
        case SyncConnected:
          connected = true;
          break;

        case Disconnected:
          connected = false;
          break;

        case Expired:
          sessionExpired = true;
          connected = false;
          LOG.error("Session expired!!!");

        default:
          break;
        }
      }

    }

  };

  public boolean isConnected() {
    return connected;
  }

  public boolean isSessionExpired() {
    return sessionExpired;
  }

  public void startZK() throws IOException {
    zk = new ZooKeeper(hostAndPort, 15000, clientWatcher);
  }

  public void stopZK() throws InterruptedException {
    zk.close();
  }

  /**
   * Executes a sample task and watches for the result
   * @see #createTaskCallback
   */
  public void submitTask(String task, TaskObject taskCtx) {
    taskCtx.setTask(task);

    zk.create(MasterWorkerConstants.TASK_ZNODE_PATH_PREFIX, //
      task.getBytes(), //
      MasterWorkerConstants.DEFAULT_ACLS, //
      CreateMode.PERSISTENT_SEQUENTIAL, //
      createTaskCallback, //
      taskCtx//
    );
  }

  /**
   * @see #submitTask(String, TaskObject)
   * @see #watchStatus(String, Object)
   */
  private StringCallback createTaskCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        submitTask(((TaskObject) ctx).getTask(), (TaskObject) ctx);
        break;

      case OK:
        LOG.info("My created task name: " + name);
        ((TaskObject) ctx).setTaskName(name);
        watchStatus(name.replace("/tasks/", "/status/"), ctx);
        break;

      default:
        LOG.error("create task failed" + KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /************************************************************************************************
   **** task execution result
   ************************************************************************************************/

  /**
   * @param path
   * @param ctx
   * @see #createTaskCallback
   * @see #statusWatcher
   * @see #existsCallback
   */
  private void watchStatus(String path, Object ctx) {
    ctxMap.put(path, ctx);

    zk.exists(//
      path, //
      statusWatcher, //
      existsCallback, //
      ctx//
    );
  }

  /**
   * @see #watchStatus(String, Object)
   * @see #getDataCallback
   */
  private Watcher statusWatcher = new Watcher() {
    @Override
    public void process(WatchedEvent event) {
      if (EventType.NodeCreated.equals(event.getType())) {

        if (event.getPath().contains(MasterWorkerConstants.STATUS_TASK_ZNODE_PATH_PREFIX)//
            && ctxMap.containsKey(event.getPath())) {

          zk.getData(//
            event.getPath(), //
            false, //
            getDataCallback, //
            ctxMap.get(event.getPath())//
          );
        }

      }
    }
  };

  /**
   * @see #watchStatus
   * @see #getDataCallback
   */
  private StatCallback existsCallback = new StatCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        watchStatus(path, ctx);
        break;

      case OK:
        if (stat != null) {
          zk.getData(//
            path, //
            false, //
            getDataCallback, //
            ctx//
          );
          LOG.info("Status node is there: " + path);
        }
        break;

      case NONODE:
        break;

      default:
        LOG.error("check existence of staut znode failed",
          KeeperException.create(Code.get(rc), path));
        break;
      }
    }
  };

  /**
   * @see #existsCallback
   * @see #taskDeleteCallback
   */
  private DataCallback getDataCallback = new DataCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        zk.getData(path, false, getDataCallback, ctxMap.get(path));
        return;

      case OK:
        String taskResult = new String(data);
        LOG.info("Task " + path + ", " + taskResult);

        if (ctx != null) {
          ((TaskObject) ctx).setStatus(taskResult.contains("done"));

          zk.delete(path, -1, taskDeleteCallback, null);

          ctxMap.remove(path);
        }

        break;

      case NONODE:
        LOG.warn("Status node is gone!");
        return;

      default:
        LOG.error("get data of status znode failed", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * @see #getDataCallback
   */
  private VoidCallback taskDeleteCallback = new VoidCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        zk.delete(path, -1, taskDeleteCallback, null);
        break;

      case OK:
        LOG.info("Successfully deleted " + path);
        break;

      default:
        LOG.error("delete task failed", KeeperException.create(Code.get(rc), path));
      }
    }
  };

}
