package com.spike.giantdataanalysis.procedure.mw;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
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

/**
 * <pre>
 * The Worker Implementation
 * 
 * should watch: 
 * 1 new task assignments
 * </pre>
 * 
 * @author zhoujiagen
 */
public class Worker {
  private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

  // worker's ID
  private String serverId = Integer.toHexString((new Random()).nextInt());
  // worker's name
  private String workerName = "worker-" + serverId;

  // the Zookeeper handle
  private ZooKeeper zk;

  // the ZooKeeper Service connection
  private String hostAndPort;

  // flags about connection and session
  private volatile boolean connected = false;
  private volatile boolean sessionExpired = false;

  private Executor executor;
  private int executionCount;

  // status of worker
  private String workerStatus;

  // assigned task cache
  private ChildrenCache assignedTasksCache = new ChildrenCache();

  // workerWatcher's watcher, the default watcher
  private final Watcher workerWatcher = new Watcher() {
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

  public Worker(String hostAndPort) {
    this.hostAndPort = hostAndPort;
    executor = new ThreadPoolExecutor(1, 1, 1000L, TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<Runnable>(200));
  }

  public static void main(String[] args) throws InterruptedException, IOException {
    Worker worker = new Worker(MasterWorkerConstants.DEFAULT_QUORUM_CONN_STRING);

    worker.startZK();

    while (!worker.isConnected()) {
      Thread.sleep(100L);
    }

    worker.bootstrap();

    worker.register();

    worker.getTasks();

    while (!worker.isSessionExpired()) {
      Thread.sleep(1000L);
    }

  }

  public boolean isConnected() {
    return connected;
  }

  public boolean isSessionExpired() {
    return sessionExpired;
  }

  @Override
  public String toString() {
    return "Worker [serverId=" + serverId + "]";
  }

  public void startZK() throws IOException {
    zk = new ZooKeeper(this.hostAndPort, 15000, workerWatcher);
  }

  public void stopZK() throws InterruptedException {
    zk.close();
  }

  /**
   * create metadata znode of workers
   */
  private void bootstrap() {
    createAssignZnode();

  }

  /**
   * create assign znode
   * @see #createAssignCallback
   */
  private void createAssignZnode() {
    zk.create(//
      MasterWorkerConstants.ASSIGN_WORKER_ZNONE_PATH_PREFIX + serverId, //
      new byte[0], //
      MasterWorkerConstants.DEFAULT_ACLS, //
      CreateMode.PERSISTENT, //
      createAssignCallback, //
      null//
    );
  }

  /**
   * @see #createAssignZnode()
   */
  private StringCallback createAssignCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        createAssignZnode();// chain to create again
        break;

      case OK:
        LOG.info("assign worker zonde created.");
        break;

      case NODEEXISTS:
        LOG.warn("assign worker znode already existed.");
        break;

      default:
        LOG.error("create assign worker znode failed", KeeperException.create(Code.get(rc), path));
      }

    }
  };

  /**
   * worker register itself to master
   * @see #createWorkerCallback
   */
  private void register() {

    zk.create(//
      MasterWorkerConstants.WORKER_PARENT_ZNODE_PATH_PREFIX + workerName, //
      "Idle".getBytes(), //
      MasterWorkerConstants.DEFAULT_ACLS, //
      CreateMode.EPHEMERAL, // ephemeral mode
      createWorkerCallback, //
      null//
    );

  }

  /**
   * @see #register()
   */
  private StringCallback createWorkerCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        register();// chain to create again
        break;

      case OK:
        LOG.info("register worker[" + workerName + "] successfully");
        break;

      case NODEEXISTS:
        LOG.warn("worker[" + workerName + "] already resiterd");
        break;

      default:
        LOG.error("register worker failed", KeeperException.create(Code.get(rc), path));
      }

    }
  };

  /**
   * update status of worker
   * @param status
   * @see #statusUpdateCallback
   */
  private synchronized void updateWorkerStatus(String status) {
    if (this.workerStatus.equals(status)) {
      zk.setData(//
        MasterWorkerConstants.WORKER_PARENT_ZNODE_PATH_PREFIX + workerName, //
        status.getBytes(), //
        -1, //
        statusUpdateCallback, //
        status//
      );
    }
  }

  /**
   * @see #updateWorkerStatus(String)
   */
  private StatCallback statusUpdateCallback = new StatCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        updateWorkerStatus((String) ctx);
        return;

      default:
        break;
      }
    }
  };

  public void setStatus(String status) {
    this.workerStatus = status;
    updateWorkerStatus(status);
  }

  /**
   * TODO change execution count
   * @param countChange
   */
  synchronized void changeExecutionCount(int countChange) {
    executionCount += countChange;
    if (executionCount == 0 && countChange < 0) {
      // we have just become idle
      setStatus("Idle");
    }
    if (executionCount == 1 && countChange > 0) {
      // we have just become idle
      setStatus("Working");
    }
  }

  /************************************************************************************************
   **** new task assignments
   ************************************************************************************************/

  /**
   * get tasks
   * @see #newTaskWatcher
   * @see #getTaskCallback
   */
  private void getTasks() {
    zk.getChildren(//
      MasterWorkerConstants.ASSIGN_PARENT_ZNODE_PATH_PREFIX + workerName, //
      newTaskWatcher, //
      getTaskCallback, //
      null//
    );

  }

  /**
   * @see #getTasks()
   */
  private Watcher newTaskWatcher = new Watcher() {
    @Override
    public void process(WatchedEvent event) {

      if (EventType.NodeChildrenChanged.equals(event.getType())) {

        if ((MasterWorkerConstants.ASSIGN_PARENT_ZNODE_PATH_PREFIX + workerName)
            .equals(event.getPath())) {
          getTasks();
        }
      }
    }
  };

  /**
   * @see #getTasks()
   * @see #taskDataCallback
   */
  private ChildrenCallback getTaskCallback = new ChildrenCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getTasks();// chain to get again
        break;

      case OK:
        if (CollectionUtils.isNotEmpty(children)) {
          executor.execute(new Runnable() {
            List<String> children;
            DataCallback cb;

            public Runnable init(List<String> children, DataCallback cb) {
              this.children = children;
              this.cb = cb;

              return this;
            }

            public void run() {
              if (children == null) {
                return;
              }

              LOG.info("Looping into tasks");
              setStatus("Working");

              for (String task : children) {
                zk.getData(//
                  MasterWorkerConstants.ASSIGN_WORKER_ZNONE_PATH_PREFIX + serverId + "/" + task, //
                  false, //
                  cb, //
                  task//
                );
              }
            }
          }.init(assignedTasksCache.addedAndSet(children), taskDataCallback));
        }

        break;

      default:
        LOG.error("get tasks failed", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * @see #getTaskCallback
   * @see #taskStatusCreateCallback
   * @see #taskVoidCallback
   */
  private DataCallback taskDataCallback = new DataCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        zk.getData(path, false, taskDataCallback, null);
        break;

      case OK:

        executor.execute(new Runnable() {
          byte[] data;
          Object ctx;

          public Runnable init(byte[] data, Object ctx) {
            this.data = data;
            this.ctx = ctx;

            return this;
          }

          @Override
          public void run() {
            LOG.info("Executing your task: " + new String(data));
            zk.create(//
              MasterWorkerConstants.STATUS_PARENT_ZNODE_PATH_PREFIX + (String) ctx, //
              "done".getBytes(), //
              MasterWorkerConstants.DEFAULT_ACLS, //
              CreateMode.PERSISTENT, //
              taskStatusCreateCallback, //
              null//
            );

            zk.delete(//
              MasterWorkerConstants.ASSIGN_WORKER_ZNONE_PATH_PREFIX + serverId + "/" + (String) ctx, //
              -1, //
              taskVoidCallback, //
              null//
            );
          }
        }.init(data, ctx));

        break;

      default:
        LOG.error("get task data failed", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * @see #taskDataCallback
   */
  private StringCallback taskStatusCreateCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        zk.create(//
          path + "/status", //
          "done".getBytes(), //
          MasterWorkerConstants.DEFAULT_ACLS, //
          CreateMode.PERSISTENT, //
          taskStatusCreateCallback, //
          null//
        );
        break;

      case OK:
        LOG.info("Created status znode correctly: " + name);
        break;

      case NODEEXISTS:
        LOG.warn("Node exists: " + path);
        break;

      default:
        LOG.error("Failed to create task result data: ",
          KeeperException.create(Code.get(rc), path));
      }

    }
  };

  /**
   * @see #taskDataCallback
   */
  private VoidCallback taskVoidCallback = new VoidCallback() {
    @Override
    public void processResult(int rc, String path, Object rtx) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        break;

      case OK:
        LOG.info("Task correctly deleted: " + path);
        break;

      default:
        LOG.error("Failed to delete task data" + KeeperException.create(Code.get(rc), path));
      }
    }
  };

}
