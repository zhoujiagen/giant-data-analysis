package com.spike.giantdataanalysis.task.store.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spike.giantdataanalysis.task.store.domain.TaskAssignment;
import com.spike.giantdataanalysis.task.store.domain.TaskExecution;
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

  // ======================================== 创建任务相关功能
  /**
   * 创建任务定义
   * @param taskInfo
   * @return
   * @throws TaskStoreException
   */
  @Transactional
  public long createTask(TaskInfo taskInfo) throws TaskStoreException {
    TaskInfo rtnTaskInfo = taskInfoDao.save(taskInfo);
    return rtnTaskInfo.getId();
  }

  /**
   * 查询运行中的任务定义
   * @return
   * @throws TaskStoreException
   */
  public List<TaskInfo> queryRunningTasks() throws TaskStoreException {
    return taskInfoDao.queryByTaskStatus(TaskExecution.TaskStatus.DOING);
  }

  /**
   * 查询未指派的重做任务定义
   * @return
   * @throws TaskStoreException
   */
  public List<TaskInfo> queryUnassignedRedoTask() throws TaskStoreException {
    return taskInfoDao
        .queryByAllType(TaskInfo.TaskType.REDO, false, TaskExecution.TaskStatus.READY);
  }

  /**
   * 查询未指派的任务定义
   * @return
   * @throws TaskStoreException
   */
  public List<TaskInfo> queryUnassignedTask() throws TaskStoreException {
    return taskInfoDao.queryByAssigned(false);
  }

  /**
   * 查询最近执行完成的常规任务(包括完成和异常终止)
   * @param workloadShardKeyList
   * @return
   */
  public List<TaskInfo> queryLastFinishedTaskInfo(Set<String> workloadShardKeyList)
      throws TaskStoreException {
    return taskInfoDao.queryLastFinishedRegularTaskInfos(workloadShardKeyList);
  }

  /**
   * 查询最近的常规任务
   * @param workloadShardKeyList
   * @return
   * @see #queryLastFinishedTaskInfo(Set)
   */
  public List<TaskInfo> queryLastTaskInfo(Set<String> workloadShardKeyList)
      throws TaskStoreException {
    return taskInfoDao.queryLastRegularTaskInfos(workloadShardKeyList);
  }

  // ======================================== 指派任务相关功能
  /**
   * 创建任务指派
   * <p>
   * 副作用: 更新任务定义中指派状态.
   * @param taskAssignment
   * @return
   * @throws TaskStoreException
   */
  @Transactional
  public long assignTask(TaskAssignment taskAssignment) throws TaskStoreException {
    TaskAssignment rtnTaskAssignment = taskAssignmentDao.save(taskAssignment);

    // 更新任务定义中指派状态
    taskInfoDao.updateAssigned(taskAssignment.getTaskId(), true);

    return rtnTaskAssignment.getId();
  }

  /**
   * 查询工作者未获取的任务指派
   * @param workerId
   * @return
   * @throws TaskStoreException
   */
  public List<TaskAssignment> myUnTakedTasks(String workerId) throws TaskStoreException {
    return taskAssignmentDao.queryUnTakedTasks(workerId);
  }

  /**
   * 获取任务指派
   * @param taskInfoId
   * @param workerId
   */
  @Transactional
  public void takeTaskAssignment(long taskInfoId, String workerId) throws TaskStoreException {
    taskAssignmentDao.updateTaked(taskInfoId, workerId, true);
  }

  // ======================================== 记录任务进展相关功能
  /**
   * 记录任务进展
   * @param taskId
   * @return
   * @throws TaskStoreException
   */
  @Transactional
  public TaskExecution updateTaskProgress(TaskExecution taskExecution) throws TaskStoreException {
    TaskExecution result = taskExecutionDao.saveAndFlush(taskExecution);

    // 更新任务定义中任务执行状态
    long taskInfoId = taskExecution.getTaskInfoId();
    if (taskInfoId > 0l) {
      taskInfoDao.updateTaskStatus(taskInfoId, taskExecution.getTaskStatus());
    }

    return result;
  }

}
