package com.spike.giantdataanalysis.task.execution.core.context;

import java.util.Set;

import com.spike.giantdataanalysis.task.execution.config.TaskExecutionProperties;
import com.spike.giantdataanalysis.task.store.service.TaskStoreService;

public interface TaskExecutionContext {

  /**
   * 任务相关实体服务
   * @return
   */
  TaskStoreService taskStoreService();

  /**
   * 应用配置
   * @return
   */
  TaskExecutionProperties config();

  /**
   * 工作者集合
   * @return
   */
  Set<String> workers();

  /**
   * 自己的工作者ID
   * @return
   */
  String myWorkId();
}
