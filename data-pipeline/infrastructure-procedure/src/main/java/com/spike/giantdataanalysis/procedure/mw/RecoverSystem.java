package com.spike.giantdataanalysis.procedure.mw;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecoverSystem {

  private static final Logger LOG = LoggerFactory.getLogger(RecoverSystem.class);

  // system storage
  private List<String> tasks;
  private List<String> assignments;
  private List<String> status;
  private List<String> activeWorkers;
  private List<String> assignedWorkers;

  // the ZooKeeper handle
  private ZooKeeper zk;

  public interface RecoverCallback {
    enum Result {
      OK, FAILED
    }

    public void doWork(Result result, List<String> tasks);
  }

  private RecoverCallback recoverCallback;

  public RecoverSystem(ZooKeeper zk) {
    this.zk = zk;
    this.assignments = new ArrayList<String>();
  }

  public void recover(RecoverCallback recoverCallback) {
    LOG.info("try to recover system state...");

    this.recoverCallback = recoverCallback;
    getTasks();
  }

  /**
   * get all tasks
   * @see #getTasksCallback
   */
  private void getTasks() {
    zk.getChildren(//
      MasterWorkerConstants.TASK_PARENT_ZNODE_PATH,//
      false, //
      getTasksCallback,//
      null//
    );
  }

  /**
   * @see #getTasks()
   */
  private ChildrenCallback getTasksCallback = new ChildrenCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getTasks();// chain to get again
        break;

      case OK:
        LOG.info("getting tasks for recovery");
        tasks = children;
        getAssignedWorkers();
        break;

      default:
        LOG.error("get tasks failed", KeeperException.create(Code.get(rc), path));
        recoverCallback.doWork(RecoverCallback.Result.FAILED, null);
        break;
      }
    }

  };

  /**
   * get all assigned workers
   * @see #getAssignedWorkersCallback
   */
  private void getAssignedWorkers() {
    zk.getChildren(//
      MasterWorkerConstants.ASSIGN_PARENT_ZNODE_PATH, //
      false, //
      getAssignedWorkersCallback, //
      null//
    );
  }

  /**
   * @see #getAssignedWorkers()
   */
  private ChildrenCallback getAssignedWorkersCallback = new ChildrenCallback() {

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getAssignedWorkers();// chain to get again
        break;

      case OK:
        assignedWorkers = children;
        getWorkers(assignedWorkers);

      default:
        LOG.error("get assigned workers failed", KeeperException.create(Code.get(rc), path));

        recoverCallback.doWork(RecoverCallback.Result.FAILED, null);
      }

    }

  };

  /**
   * get workers
   * @param assignedWorkers
   * @see #getWorkersCallback
   */
  private void getWorkers(/* List<String> */Object assignedWorkers) {
    zk.getChildren(//
      MasterWorkerConstants.WORKER_PARENT_ZNODE_PATH,//
      false,//
      getWorkersCallback,//
      assignedWorkers//
    );
  }

  /**
   * @see #getWorkers(List)
   */
  private ChildrenCallback getWorkersCallback = new ChildrenCallback() {

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getWorkers(ctx);// chain to get again
        break;

      case OK:
        LOG.info("getting workers assignments for recovery: " + children);

        if (children.size() == 0) {
          LOG.warn("empty list of workers!!!");
          recoverCallback.doWork(RecoverCallback.Result.OK, new ArrayList<String>());
          break;
        }

        activeWorkers = children;
        for (String worker : assignedWorkers) {
          getWorkerAssignments(MasterWorkerConstants.ASSIGN_PARENT_ZNODE_PATH_PREFIX + worker);
        }

      default:

        LOG.error("get workers failed", KeeperException.create(Code.get(rc), path));
      }

    }
  };

  private void getWorkerAssignments(String assignWorkerPath) {
    zk.getChildren(//
      assignWorkerPath, //
      false, //
      getWorkerAssignmentsCallback, //
      null//
    );
  }

  private ChildrenCallback getWorkerAssignmentsCallback = new ChildrenCallback() {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getWorkerAssignments(path);// chain to get again
        break;

      case OK:
        String worker = path.replace(MasterWorkerConstants.ASSIGN_PARENT_ZNODE_PATH_PREFIX, "");

        if (activeWorkers.contains(worker)) {

          assignments.addAll(children);

        } else {

          for (String task : children) {
            if (!tasks.contains(task)) {
              tasks.add(task);
              getDataReassign(path, task);
            } else {
              deleteAssignment(path + "/" + task);
            }
          }

          deleteAssignment(path);
        }

        assignedWorkers.remove(worker);

        if (assignedWorkers.size() == 0) {
          LOG.info("getting status for recovery");
          getStatuses();
        }

        break;

      case NONODE:
        LOG.info("no such znode exists: " + path);
        break;

      default:
        LOG.error("get worker assignments failed", KeeperException.create(Code.get(rc), path));
        recoverCallback.doWork(RecoverCallback.Result.FAILED, null);
      }
    }
  };

  /**
   * get data ressigned
   * @param path
   * @param task
   * @see #getDataReassignCallback
   */
  private void getDataReassign(String path, String task) {
    zk.getData(//
      path, //
      false, //
      getDataReassignCallback, //
      task);
  }

  class RecreateTaskCtx {
    String path;
    String task;
    byte[] data;

    RecreateTaskCtx(String path, String task, byte[] data) {
      this.path = path;
      this.task = task;
      this.data = data;
    }
  }

  /**
   * @see #getDataReassign(String, String)
   */
  private DataCallback getDataReassignCallback = new DataCallback() {

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getDataReassign(path, (String) ctx);// chain to get again
        break;

      case OK:
        recreateTask(new RecreateTaskCtx(path, (String) ctx, data));
        break;

      default:
        LOG.error("get data ressign faile", KeeperException.create(Code.get(rc), path));
      }

    }

  };

  /**
   * re-create task
   * @param ctx
   * @see #recreateTaskCallback
   */
  private void recreateTask(RecreateTaskCtx ctx) {
    zk.create(//
      MasterWorkerConstants.TASK_ZNODE_PATH_PREFIX + ctx.task,//
      ctx.data,//
      MasterWorkerConstants.DEFAULT_ACLS,//
      CreateMode.PERSISTENT,//
      recreateTaskCallback,//
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
        LOG.warn("Node shouldn't exist: " + path);

        break;
      default:
        LOG.error("recreate task fialed", KeeperException.create(Code.get(rc)));
      }
    }
  };

  /**
   * delete assignment
   * @param path
   * @see #taskDeletionCallback
   */
  private void deleteAssignment(String path) {
    zk.delete(//
      path, //
      -1, //
      taskDeletionCallback, //
      null//
    );
  }

  /**
   * @see #deleteAssignment(String)
   */
  private VoidCallback taskDeletionCallback = new VoidCallback() {
    public void processResult(int rc, String path, Object rtx) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        deleteAssignment(path);
        break;

      case OK:
        LOG.info("Task correctly deleted: " + path);
        break;

      default:
        LOG.error("Failed to delete task data" + KeeperException.create(Code.get(rc), path));
      }
    }
  };

  private void getStatuses() {
    zk.getChildren(//
      MasterWorkerConstants.STATUS_PARENT_ZNODE_PATH, //
      false, //
      statusCallback, //
      null//
    );
  }

  ChildrenCallback statusCallback = new ChildrenCallback() {
    public void processResult(int rc, String path, Object ctx, List<String> children) {
      switch (KeeperException.Code.get(rc)) {
      case CONNECTIONLOSS:
        getStatuses();
        break;

      case OK:
        LOG.info("Processing assignments for recovery");
        status = children;
        processAssignments();
        break;

      default:
        LOG.error("getChildren failed", KeeperException.create(Code.get(rc), path));
        recoverCallback.doWork(RecoverCallback.Result.FAILED, null);
      }
    }
  };

  private void processAssignments() {
    LOG.info("Size of tasks: " + tasks.size());
    // Process list of pending assignments
    for (String s : assignments) {
      LOG.info("Assignment: " + s);
      deleteAssignment(MasterWorkerConstants.TASK_PARENT_ZNODE_PATH_PREFIX + s);
      tasks.remove(s);
    }

    LOG.info("Size of tasks after assignment filtering: " + tasks.size());

    for (String s : status) {
      LOG.info("Checking task: {} ", s);
      deleteAssignment(MasterWorkerConstants.TASK_PARENT_ZNODE_PATH_PREFIX + s);
      tasks.remove(s);
    }
    LOG.info("Size of tasks after status filtering: " + tasks.size());

    // Invoke callback
    recoverCallback.doWork(RecoverCallback.Result.OK, tasks);
  }

}
