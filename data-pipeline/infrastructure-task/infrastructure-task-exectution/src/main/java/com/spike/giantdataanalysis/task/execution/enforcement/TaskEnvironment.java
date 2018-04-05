package com.spike.giantdataanalysis.task.execution.enforcement;

import java.util.Set;

/**
 * 任务环境工具类.
 * @author zhoujiagen
 */
public class TaskEnvironment {

  private static final TaskEnvironment INSTANCE = new TaskEnvironment();

  private TaskEnvironment() {
  }

  public static TaskEnvironment I() {
    return INSTANCE;
  }

  /**
   * 获取当前可用的Worker标识集合.
   * @return
   */
  public Set<String> avaiableWorkers() {
    throw new UnsupportedOperationException();
  }

  /**
   * 获取额外的负载: 常规负载和补做负载.
   * @return
   */
  public Set<String> moreWorkload() {
    throw new UnsupportedOperationException();
  }

}
