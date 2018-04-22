package com.spike.giantdataanalysis.task.execution.application.core;

import com.spike.giantdataanalysis.task.execution.core.context.TaskExecutionContext;
import com.spike.giantdataanalysis.task.execution.exception.TaskExecutionException;

/**
 * 应用负载处理器接口.
 * <p>
 * 约束: 根据指派的ID获取负载.
 * @author zhoujiagen
 */
public interface ApplicationWorkloadHandler {

  /**
   * 指派处理器标识
   * @param id
   */
  void assignId(String id);

  /**
   * 处理负载
   * @param context 任务执行上下文
   * @throws TaskExecutionException
   */
  void handle(TaskExecutionContext context) throws TaskExecutionException;
}
