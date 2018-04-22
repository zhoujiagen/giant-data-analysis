package com.spike.giantdataanalysis.task.execution.core.threads;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class TaskThreads {
  private static final Logger LOG = LoggerFactory.getLogger(TaskThreads.class);

  private static final TaskThreads INSTANCE = new TaskThreads();

  private TaskThreads() {
  }

  public static TaskThreads I() {
    return INSTANCE;
  }

  /** 线程信息集合 */
  private Set<ThreadInfo> threads = Sets.newConcurrentHashSet();

  public synchronized void startBackgroundDaemon(long checkAlivePeriod) {
    LOG.info("启动背景线程");

    new Thread(new BackgroundDaemon(checkAlivePeriod, INSTANCE)).start();
  }

  /** 创建线程 */
  public synchronized Thread newThread(String threadGroupName, String name, Runnable runnable) {
    LOG.info("创建thread[{}] in group[{}]", name, threadGroupName);

    ThreadGroup tg = new ThreadGroup(threadGroupName);
    Thread result = new Thread(tg, runnable, name);

    ThreadInfo ti = new ThreadInfo(threadGroupName, name, result);
    threads.add(ti);

    return result;
  }

  /** 启动线程 */
  public void start(Thread thread) {
    LOG.info("启动线程: [group: {}, name={}]" + thread.getThreadGroup().getName(), thread.getName());

    thread.start();
  }

  /** 等待线程执行完毕 */
  public void join(Thread thread) {
    LOG.info("等待线程执行完毕: [group: {}, name={}]" + thread.getThreadGroup().getName(), thread.getName());

    try {
      thread.join();
    } catch (InterruptedException e) {
      LOG.error("等待线程执行完毕失败", e);
    }
  }

  /** 线程信息快照 */
  public Set<ThreadInfo> snapshot() {
    return threads;
  }

}
