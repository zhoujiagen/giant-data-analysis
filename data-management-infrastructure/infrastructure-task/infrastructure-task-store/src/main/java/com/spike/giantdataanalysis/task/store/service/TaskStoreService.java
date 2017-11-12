package com.spike.giantdataanalysis.task.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spike.giantdataanalysis.task.store.domain.TaskInfo;
import com.spike.giantdataanalysis.task.store.exception.TaskStoreException;
import com.spike.giantdataanalysis.task.store.repository.TaskAssignmentRepository;
import com.spike.giantdataanalysis.task.store.repository.TaskExecutionRepository;
import com.spike.giantdataanalysis.task.store.repository.TaskInfoRepository;

/**
 * 任务相关实体服务
 * 
 * <pre>
 * 1. 任务定义
 * 2. 任务指派
 * 3. 任务执行结果记录
 * </pre>
 * @author zhoujiagen
 */
@Service
public class TaskStoreService {

  @Autowired
  private TaskInfoRepository taskInfoDao;
  @Autowired
  private TaskAssignmentRepository taskAssignmentDao;
  @Autowired
  private TaskExecutionRepository taskExecutionDao;

  /**
   * 创建任务
   * @param taskInfo
   * @return
   * @throws TaskStoreException
   */
  public long createTask(TaskInfo taskInfo) throws TaskStoreException {
    TaskInfo rtnTaskInfo = taskInfoDao.save(taskInfo);
    return rtnTaskInfo.getId();
  }

  /**
   * 指派任务
   * @param taskInfo
   * @return
   * @throws TaskStoreException
   */
  public long assignTask(TaskInfo taskInfo) throws TaskStoreException {
    throw new UnsupportedOperationException();
  }

  /**
   * 记录任务进展
   * @param taskId
   * @return
   * @throws TaskStoreException
   */
  public boolean updateTaskProgress(long taskId) throws TaskStoreException {
    throw new UnsupportedOperationException();
  }

}
