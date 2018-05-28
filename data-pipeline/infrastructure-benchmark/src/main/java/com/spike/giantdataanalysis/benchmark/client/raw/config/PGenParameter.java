package com.spike.giantdataanalysis.benchmark.client.raw.config;

import java.io.Serializable;

/**
 * 负载生成器的参数
 */
public class PGenParameter implements Serializable {
  private static final long serialVersionUID = 6987982249923478286L;

  /** 默认间隔执行的时间: 5分钟 */
  public static final long DEFAULT_DURATION = 5 * 60 * 1000l;
  /** 间隔执行的时间: 2分钟 */
  public static final long DURATION_2_MIN = 2 * 60 * 1000l;

  /** 工作者编号 */
  protected int workerIndex;
  /** 操作等待超时时间 */
  protected long waitTime = 10000l; // 10s
  /** 执行两次操作之间的时间间隔 */
  protected long actionDuration = 0l;

  public long getActionDuration() {
    return actionDuration;
  }

  public void setActionDuration(long actionDuration) {
    this.actionDuration = actionDuration;
  }

  public int getWorkerIndex() {
    return workerIndex;
  }

  public void setWorkerIndex(int workerIndex) {
    this.workerIndex = workerIndex;
  }

  public long getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(long waitTime) {
    this.waitTime = waitTime;
  }
}
