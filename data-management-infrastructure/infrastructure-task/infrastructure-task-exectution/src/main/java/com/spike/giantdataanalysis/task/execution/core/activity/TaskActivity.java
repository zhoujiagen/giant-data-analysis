package com.spike.giantdataanalysis.task.execution.core.activity;

import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;

/**
 * 活动实体.
 * @author zhoujiagen
 */
public interface TaskActivity extends Runnable {

  /** 实体标识 */
  String id();

  /**
   * 初始化工作.
   * @throws TaskExecutionException
   */
  void initialize() throws TaskExecutionException;

  /**
   * 判断是否激活.
   * @return
   * @throws TaskExecutionException
   */
  boolean enabled() throws TaskExecutionException;

  /**
   * 使失效.
   * @throws TaskExecutionException
   */
  void disable() throws TaskExecutionException;

  /**
   * 清理工作.
   * @throws TaskExecutionException
   */
  void clean() throws TaskExecutionException;
}
