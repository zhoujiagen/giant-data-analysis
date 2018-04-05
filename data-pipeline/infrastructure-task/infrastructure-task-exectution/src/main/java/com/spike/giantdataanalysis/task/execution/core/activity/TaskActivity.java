package com.spike.giantdataanalysis.task.execution.core.activity;

import com.spike.giantdataanalysis.task.execution.application.core.ApplicationWorkloadHandler;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;

/**
 * 活动实体.
 * @author zhoujiagen
 */
public interface TaskActivity extends Runnable {

  /** 实体标识 */
  String id();

  /**
   * 注册负载处理器
   * @param name
   * @param workloadHandler
   * @throws TaskExecutionException
   */
  void registWorkloadHandler(String name, ApplicationWorkloadHandler workloadHandler)
      throws TaskExecutionException;

  /**
   * 取消注册负载处理器
   * @param name
   * @throws TaskExecutionException
   */
  void unregistWorkloadHandler(String name) throws TaskExecutionException;

  /**
   * 是否是有效的负载处理器
   * @param workloadHandler
   * @return
   */
  boolean isValidWorkloadHandler(ApplicationWorkloadHandler workloadHandler);

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
