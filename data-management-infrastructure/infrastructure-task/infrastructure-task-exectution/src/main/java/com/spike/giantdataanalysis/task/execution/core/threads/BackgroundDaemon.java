package com.spike.giantdataanalysis.task.execution.core.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/** 背景守护线程 */
class BackgroundDaemon implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(BackgroundDaemon.class);

  private long checkAlivePeriod = 10 * 1000l;
  private volatile boolean shutdown = false;
  private final TaskThreads taskThreads;

  public BackgroundDaemon(long checkAlivePeriod, TaskThreads taskThreads) {
    Preconditions.checkArgument(checkAlivePeriod > 0l,
      "Invalid argument 'checkAlivePeriod': must be positive!");
    Preconditions.checkArgument(taskThreads != null, "Argument 'taskThreads' should not be null!");

    this.checkAlivePeriod = checkAlivePeriod;
    this.taskThreads = taskThreads;
  }

  public long getCheckAlivePeriod() {
    return checkAlivePeriod;
  }

  public synchronized void setShutdown(boolean shutdown) {
    this.shutdown = shutdown;
  }

  public synchronized boolean isShutdown() {
    return shutdown;
  }

  @Override
  public void run() {
    while (true) {
      try {
        if (this.isShutdown()) {
          break;
        }

        LOG.info("Threads Snapshot: {}", taskThreads.snapshot());

        Thread.sleep(checkAlivePeriod);

      } catch (InterruptedException e) {
        // ignore
      }
    }
  }

}