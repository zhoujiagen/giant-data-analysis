package com.spike.giantdataanalysis.zookeeper.support.masterworker;

import java.io.IOException;
import java.util.List;
import java.util.Random;

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

import com.spike.giantdataanalysis.zookeeper.support.ZooKeepers;
import com.spike.giantdataanalysis.zookeeper.support.masterworker.RecoverSystem.RecoverCallback;

/**
 * <pre>
 * The Master implementation
 * 
 * should watch: 
 * 1 leadership changes
 * 2 changes of list of workers
 * 3 new tasks to assign
 * </pre>
 * @author zhoujiagen
 */
public class Master {
  private static final Logger LOG = LoggerFactory.getLogger(Master.class);

  // random utilities for generate service ID
  Random random = new Random();

  // the Zookeeper handle
  private ZooKeeper zk;

  // the id of server that try to compete for leadership
  private String serverId = Integer.toHexString(random.nextInt());

  // the ZooKeeper Service connection
  private String hostAndPort;

  // flags about connection and session
  private volatile boolean connected = false;
  private volatile boolean sessionExpired = false;

  // system storage cache
  private ChildrenCache tasksCache;
  private ChildrenCache workersCache;

  /**
   * state of current competitor when running for master
   */
  public enum MasterState {
    ELECTED, NOTELECTED, RUNNING
  }

  private MasterState masterState = MasterState.RUNNING;

  public Master(String hostAndPort) {
    this.hostAndPort = hostAndPort;
  }

  // master's watcher, the default watcher
  private final Watcher masterWatcher = new Watcher() {
    @Override
    public void process(WatchedEvent event) {
      LOG.info(getHolder() + " got watched event: " + event);

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
          LOG.error(getHolder() + " Session expired!!!");

        default:
          break;
        }
      }
    }
  };

  public static void main(String[] args) throws InterruptedException, IOException {
    String hostAndPort = ZooKeepers.DEFAULT_QUORUM_CONN_STRING;
    Master master = new Master(hostAndPort);

    master.startZK();

    while (!master.isConnected()) {
      Thread.sleep(100L);
    }

    master.createMetadataZnode(ZooKeepers.WORKER_PARENT_ZNODE_PATH, new byte[0]);
    master.createMetadataZnode(ZooKeepers.ASSIGN_PARENT_ZNODE_PATH, new byte[0]);
    master.createMetadataZnode(ZooKeepers.TASK_PARENT_ZNODE_PATH, new byte[0]);
    master.createMetadataZnode(ZooKeepers.STATUS_PARENT_ZNODE_PATH, new byte[0]);

    master.runForMaster();

    while (!master.isSessionExpired()) {
      Thread.sleep(1000L);
    }

    master.stopZK();
  }

  public boolean isConnected() {
    return connected;
  }

  public boolean isSessionExpired() {
    return sessionExpired;
  }

  @Override
  public String toString() {
    return "Master [serverId=" + serverId + ", masterState=" + masterState + "]";
  }

  /**
   * @return watchers or callbacks' holder, i.e. the Masters
   */
  private String getHolder() {
    return this.toString();
  }

  public void startZK() throws IOException {
    zk = new ZooKeeper(this.hostAndPort, 15000, masterWatcher);
  }

  public void stopZK() throws InterruptedException {
    zk.close();
  }

  /**
   * create znode
   * @param path the znode path
   * @param data the znode data
   * @see #createMetadataZnodeCallback
   */
  private void createMetadataZnode(String path, byte[] data) {
    LOG.info(this + " try to create metadata with parameters: path=" + path + ",data="
        + new String(data));

    zk.create(//
      path, //
      data, // real data
      ZooKeepers.DEFAULT_ACLS, //
      CreateMode.PERSISTENT, //
      createMetadataZnodeCallback, //
      data// the context object
    );
  }

  /**
   * @see #createMetadataZnode(String, byte[])
   */
  private StringCallback createMetadataZnodeCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        createMetadataZnode(path, (byte[]) ctx);// chain to create again
        break;

      case OK:
        LOG.info(path + " created.");
        break;

      case NODEEXISTS:
        LOG.info(path + " already existed!!! ");
        break;

      default:
        LOG.error("create metadata znode failed", KeeperException.create(Code.get(rc), path));
      }
    }

  };

  /************************************************************************************************
   **** leadership changes
   ************************************************************************************************/
  /**
   * compete for leadership
   * @throws InterruptedException
   * @see #masterCreateCallback
   */
  public void runForMaster() {
    LOG.info(this + " run for master..");

    zk.create(//
      ZooKeepers.MASTER_ZNODE_PATH, //
      serverId.getBytes(), // the real data
      ZooKeepers.DEFAULT_ACLS, //
      CreateMode.EPHEMERAL,// ephemeral mode
      masterCreateCallback, //
      null// the context object
    );
  }

  /**
   * @see #runForMaster
   */
  private StringCallback masterCreateCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        checkMaster();// check current system state
        break;

      case OK:
        masterState = MasterState.ELECTED;
        takeLeadership();// play the active master role
        break;

      case NODEEXISTS:
        masterState = MasterState.NOTELECTED;
        masterExists();// follow the active master
        break;

      default:
        masterState = MasterState.NOTELECTED;
        LOG.error("running for master failed", KeeperException.create(Code.get(rc), path));
      }

      LOG.info("I am " + (MasterState.ELECTED.equals(masterState) ? "" : "not") + " the leader");
    }

  };

  /**
   * check whether any one competitor has gained the leadership already
   * @see #masterCheckCallback
   */
  private void checkMaster() {
    zk.getData(//
      ZooKeepers.MASTER_ZNODE_PATH, //
      false, // no need to set a watch
      masterCheckCallback, //
      null// the context object
    );
  }

  /**
   * @see #checkMaster()
   */
  private DataCallback masterCheckCallback = new DataCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        checkMaster();// chain to check again
        return;

      case NONODE:
        runForMaster();// compete for leadership
        return;

      case OK:
        if (serverId.equals(new String(data))) {// i am the master
          masterState = MasterState.ELECTED;
          takeLeadership();
        } else {
          masterState = MasterState.NOTELECTED;
          masterExists();
        }
        break;

      default:
        LOG.error("read znone data failed",
          KeeperException.create(KeeperException.Code.get(rc), path));
      }
    }
  };

  /**
   * follow current active master
   * @see #masterExistsWatcher
   * @see #masterExistsCallback
   */
  private void masterExists() {
    zk.exists(//
      ZooKeepers.MASTER_ZNODE_PATH,//
      masterExistsWatcher, //
      masterExistsCallback,//
      null//
    );
  }

  /**
   * Watcher of {@link #masterExists()}
   */
  private Watcher masterExistsWatcher = new Watcher() {
    @Override
    public void process(WatchedEvent event) {
      if (Event.EventType.NodeDeleted.equals(event.getType())) {

        // the /master znode is deleted
        if (event.getPath().equals(ZooKeepers.MASTER_ZNODE_PATH)) {
          LOG.info(getHolder() + " run for master...");
          runForMaster();
        }
      }
    }
  };

  /**
   * Callback of {@link #masterExists()}
   */
  private StatCallback masterExistsCallback = new StatCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
      switch (KeeperException.Code.get(rc)) {

      case CONNECTIONLOSS:
        masterExists();// chain to check existence again
        break;

      case OK:
        break;

      case NONODE:
        LOG.info("the master node is not existed, decide to run for master");
        masterState = MasterState.RUNNING;
        runForMaster();
        break;

      default:
        checkMaster();// try to catch up system changes
        break;
      }
    }
  };

  /**
   * play the active master role
   */
  private void takeLeadership() {
    LOG.info("I am the leader, said by [" + serverId + "].");

    getWorkers();

    RecoverSystem recoverSystem = new RecoverSystem(zk);
    RecoverCallback recoverCallback = new RecoverCallback() {
      @Override
      public void doWork(Result result, List<String> tasks) {

        if (Result.FAILED.equals(result)) {

          LOG.error("recover failed");

        } else {

          LOG.info("assign recoverd tasks...");
          getTasks();
        }
      }
    };

    recoverSystem.recover(recoverCallback);
  }

  /************************************************************************************************
   **** changes of list of workers
   ************************************************************************************************/
  /**
   * representation of context of tasks should be recreated
   */
  class RecreateTaskCtx {
    public String path;
    public String task;
    public byte[] data;

    RecreateTaskCtx(String path, String task, byte[] data) {
      this.path = path;
      this.task = task;
      this.data = data;
    }
  }

  /**
   * get available workers
   * @see #workersChangeWatcher
   * @see #getWorkersCallback
   */
  private void getWorkers() {
    LOG.info("try to get all avaiable workers...");

    zk.getChildren(//
      ZooKeepers.WORKER_PARENT_ZNODE_PATH,//
      workersChangeWatcher,//
      getWorkersCallback,//
      null//
    );
  }

  /**
   * @see #getWorkers()
   */
  private Watcher workersChangeWatcher = new Watcher() {
    public void process(WatchedEvent event) {
      if (EventType.NodeChildrenChanged.equals(event.getType())) {

        if (ZooKeepers.WORKER_PARENT_ZNODE_PATH.equals(event.getPath())) {
          getWorkers();
        }
      }
    }
  };

  /**
   * @see #getWorkers
   */
  private ChildrenCallback getWorkersCallback = new ChildrenCallback() {
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getWorkers();// chain to get again
        break;

      case OK:
        LOG.info("got " + children.size() + " workers");
        reassignAndSet(children);
        break;

      default:
        LOG.error("get /workers children failed", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * update system workers cache, and<br>
   * get absent workers task
   * @param children
   * @see #getWorkersCallback
   * @see #getAbsentWorkerTasks(String)
   */
  private void reassignAndSet(List<String> children) {
    List<String> toProcess;

    if (workersCache == null) {
      workersCache = new ChildrenCache(children);
      toProcess = null;
    } else {
      // get absent workers
      toProcess = workersCache.removedAndSet(children);
    }

    if (toProcess != null) {
      for (String worker : toProcess) {
        getAbsentWorkerTasks(worker);
      }
    }
  }

  /**
   * get absent worker's task and
   * @param worker
   * @see #reassignAndSet(List)
   * @see #workerAssignmentCallback
   */
  private void getAbsentWorkerTasks(String worker) {
    zk.getChildren(//
      ZooKeepers.ASSIGN_PARENT_ZNODE_PATH_PREFIX + worker,//
      false, //
      workerAssignmentCallback, //
      null//
    );
  }

  /**
   * @see #getAbsentWorkerTasks(String)
   * @see #getDataReassign(String, String)
   */
  private ChildrenCallback workerAssignmentCallback = new ChildrenCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      // path is: /assign/worker-xxx

      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getAbsentWorkerTasks(path);// chain to get again
        break;

      case OK:
        LOG.info("got previous assignments: " + children.size() + " tasks");
        for (String task : children) {
          getDataReassign(path + "/" + task, task);
        }
        break;

      default:
        LOG.error("get absent worker's assignment failed",
          KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * get absent work's assigned tasks
   * @param path
   * @param task
   * @see #getDataReassignCallback
   */
  private void getDataReassign(String path, String task) {
    zk.getData(//
      path, //
      false, //
      getDataReassignCallback, //
      task//
    );
  }

  /**
   * @see #getDataReassign(String, String)
   * @see #recreateTask(RecreateTaskCtx)
   */
  private DataCallback getDataReassignCallback = new DataCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getDataReassign(path, (String) ctx);
        break;

      case OK:
        recreateTask(new RecreateTaskCtx(path, (String) ctx, data));
        break;

      default:
        LOG.error("get assigned task's data failed ", KeeperException.create(Code.get(rc)));
      }
    }
  };

  /**
   * recreate tasks that has been assigned to absent workers
   * @param ctx
   * @see #getDataReassignCallback
   * @see #recreateTaskCallback
   */
  private void recreateTask(RecreateTaskCtx ctx) {
    zk.create(//
      ZooKeepers.TASK_PARENT_ZNODE_PATH_PREFIX + ctx.task, //
      ctx.data, //
      ZooKeepers.DEFAULT_ACLS, //
      CreateMode.PERSISTENT,//
      recreateTaskCallback, //
      ctx//
    );
  }

  /**
   * @see #recreateTask(RecreateTaskCtx)
   */
  private StringCallback recreateTaskCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        recreateTask((RecreateTaskCtx) ctx);
        break;

      case OK:
        deleteAssignment(((RecreateTaskCtx) ctx).path);
        break;

      case NODEEXISTS:
        // why not just delete the assignment???
        // is the reason that about the znode data???
        LOG.info("Node exists already, but if it hasn't been deleted, "
            + "then it will eventually, so we keep trying: " + path);
        recreateTask((RecreateTaskCtx) ctx);
        break;

      default:
        LOG.error("recreate task failed", KeeperException.create(Code.get(rc)));
      }
    }
  };

  /**
   * @see #taskDeletionCallback
   * @param path
   */
  private void deleteAssignment(String path) {
    zk.delete(//
      path,//
      -1, // cover all versions
      taskDeletionCallback, //
      null//
    );
  }

  /**
   * @see #deleteAssignment(String)
   */
  private VoidCallback taskDeletionCallback = new VoidCallback() {
    @Override
    public void processResult(int rc, String path, Object rtx) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        deleteAssignment(path);// chain to delete again
        break;

      case OK:
        LOG.info("Task correctly deleted: " + path);
        break;

      default:
        LOG.error("Failed to delete task", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /************************************************************************************************
   **** new tasks to assign
   ************************************************************************************************/
  /**
   * get tasks
   * @see #tasksChangeWatcher
   * @see #tasksGetChildrenCallback
   */
  private void getTasks() {
    zk.getChildren(//
      ZooKeepers.TASK_PARENT_ZNODE_PATH,//
      tasksChangeWatcher, //
      tasksGetChildrenCallback, //
      null//
    );
  }

  /**
   * @see #getTasks()
   */
  private Watcher tasksChangeWatcher = new Watcher() {
    @Override
    public void process(WatchedEvent event) {
      if (EventType.NodeChildrenChanged.equals(event.getType())) {

        if (ZooKeepers.TASK_PARENT_ZNODE_PATH.equals(event.getPath())) {
          getTasks();// get new tasks
        }
      }
    }
  };

  /**
   * @see #getTasks()
   */
  private ChildrenCallback tasksGetChildrenCallback = new ChildrenCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getTasks();
        break;

      case OK:
        List<String> toProcess;

        if (tasksCache == null) {
          tasksCache = new ChildrenCache(children);
          toProcess = children;
        } else {
          toProcess = tasksCache.addedAndSet(children);
        }

        if (toProcess != null) {
          assignTasks(toProcess);
        }

        break;

      default:
        LOG.error("getChildren failed.", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * assgin tasks
   * @param tasks
   */
  private void assignTasks(List<String> tasks) {
    for (String task : tasks) {
      getTaskData(task);
    }
  }

  /**
   * get data of task
   * @see #taskDataCallback
   */
  private void getTaskData(String task) {
    zk.getData(//
      ZooKeepers.TASK_PARENT_ZNODE_PATH_PREFIX + task, //
      false, //
      taskDataCallback, //
      task//
    );
  }

  /**
   * @see #getTaskData(String)
   */
  private DataCallback taskDataCallback = new DataCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getTaskData((String) ctx);
        break;

      case OK:
        List<String> list = workersCache.getChildren();
        // choose one available worker
        String worker = list.get(random.nextInt(list.size()));
        String assignmentPath =
            ZooKeepers.ASSIGN_PARENT_ZNODE_PATH_PREFIX + worker + "/" + (String) ctx;

        LOG.info("Assignment path: " + assignmentPath);
        createAssignment(assignmentPath, data);
        break;

      default:
        LOG.error("get task's data failed", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * create assignment
   * @param path
   * @param data
   * @see #assignTaskCallback
   */
  private void createAssignment(String path, byte[] data) {
    zk.create(//
      path, //
      data, //
      ZooKeepers.DEFAULT_ACLS, //
      CreateMode.PERSISTENT, //
      assignTaskCallback, //
      data//
    );
  }

  /**
   * @see #createAssignment(String, byte[])
   */
  private StringCallback assignTaskCallback = new StringCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        createAssignment(path, (byte[]) ctx);// chain to create again
        break;

      case OK:
        LOG.info("Task assigned correctly: " + name);
        deleteTask(name.substring(name.lastIndexOf("/") + 1));
        break;

      case NODEEXISTS:
        LOG.warn("Task already assigned");
        break;

      default:
        LOG.error("assign task failed", KeeperException.create(Code.get(rc), path));
      }
    }
  };

  /**
   * delete tasks
   * @see #taskDeleteCallback
   */
  private void deleteTask(String name) {
    zk.delete(//
      ZooKeepers.TASK_PARENT_ZNODE_PATH_PREFIX + name,//
      -1, //
      taskDeleteCallback,//
      null//
    );
  }

  /**
   * @see #deleteTask(String)
   */
  private VoidCallback taskDeleteCallback = new VoidCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        deleteTask(path);// chain to delete again
        break;

      case OK:
        LOG.info("Successfully deleted " + path);
        break;

      case NONODE:
        LOG.info("Task has been deleted already");
        break;

      default:
        LOG.error("delete task failed, " + KeeperException.create(Code.get(rc), path));
      }
    }
  };
}
