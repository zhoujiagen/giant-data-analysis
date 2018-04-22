package com.spike.giantdataanalysis.task.store.repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spike.giantdataanalysis.task.store.beans.TaskInfoWorkloadShard;
import com.spike.giantdataanalysis.task.store.domain.TaskExecution;
import com.spike.giantdataanalysis.task.store.domain.TaskInfo;

@Repository
public interface TaskInfoRepository extends JpaRepository<TaskInfo, Long> {

  /**
   * 查询负载键的数量
   * @param workloadShardKeyList
   * @return
   */
  @Query("SELECT NEW com.spike.giantdataanalysis.task.store.beans.TaskInfoWorkloadShard(t.workloadShardKey, COUNT(t.workloadShardKey) AS cnt) "//
      + "FROM TaskInfo t "//
      + "WHERE t.workloadShardKey IN ?1 "//
      + "GROUP BY t.workloadShardKey")
  public
      List<TaskInfoWorkloadShard> queryWorkloadShardCount(Set<String> workloadShardKeyList);

  /**
   * 查询最近执行完成的常规任务(包括完成和异常终止)
   * @param workloadShardKeyList
   * @return
   */
  @Query(value = "select t.*  "//
      + "from t_gda_task_info t "//
      + "inner join ( "//
      + "   select t.workload_shardkey, max(t.id) id "//
      + "   from t_gda_task_info t "//
      + "   where t.task_type = 'REGUALR' and "//
      + "     (t.task_status = 'DONE' or t.task_status = 'CORRUPT') "//
      + "   group by t.workload_shardkey"//
      + ") v on t.id = v.id "//
      + "where t.workload_shardkey in ?1", nativeQuery = true)
  public List<TaskInfo> queryLastFinishedRegularTaskInfos(Set<String> workloadShardKeyList);

  /**
   * 查询最近的常规任务(包括完成和异常终止)
   * @param workloadShardKeyList
   * @return
   * @see #queryLastFinishedRegularTaskInfos(Set)
   */
  @Query(value = "select t.*  "//
      + "from t_gda_task_info t "//
      + "inner join ( "//
      + "   select t.workload_shardkey, max(t.id) id "//
      + "   from t_gda_task_info t "//
      + "   where t.task_type = 'REGUALR' "//
      + "   group by t.workload_shardkey"//
      + ") v on t.id = v.id "//
      + "where t.workload_shardkey in ?1", nativeQuery = true)
  public List<TaskInfo> queryLastRegularTaskInfos(Set<String> workloadShardKeyList);

  /**
   * 按类型查询任务定义
   * @param taskType
   * @param assigned
   * @param taskStatus
   * @return
   */
  @Query("SELECT t FROM TaskInfo t "//
      + "WHERE t.taskType = ?1 AND t.assigned = ?2 AND t.taskStatus = ?3")
  public List<TaskInfo> queryByAllType(TaskInfo.TaskType taskType, boolean assigned,
      TaskExecution.TaskStatus taskStatus);

  /** 按任务类型(taskType)查询任务定义 */
  @Query("SELECT t FROM TaskInfo t WHERE t.taskType = ?1")
  public List<TaskInfo> queryByTaskType(TaskInfo.TaskType taskType);

  /** 按是否指派(assigned)查询任务定义 */
  @Query("SELECT t FROM TaskInfo t WHERE t.assigned = ?1")
  public List<TaskInfo> queryByAssigned(boolean assigned);

  /** 按任务状态(taskStatus)查询任务定义 */
  @Query("SELECT t FROM TaskInfo t WHERE t.taskStatus = ?1")
  public List<TaskInfo> queryByTaskStatus(TaskExecution.TaskStatus taskStatus);

  /**
   * 更新指派状态
   * @param id
   * @param assigned
   */
  @Transactional
  @Modifying
  @Query("UPDATE TaskInfo SET assigned = ?2 WHERE id = ?1")
  public void updateAssigned(Long id, boolean assigned);

  /**
   * 更新任务执行状态
   * @param id
   * @param taskStatus
   */
  @Transactional
  @Modifying
  @Query("UPDATE TaskInfo SET taskStatus = ?2 WHERE id = ?1")
  public void updateTaskStatus(Long id, TaskExecution.TaskStatus taskStatus);

}
