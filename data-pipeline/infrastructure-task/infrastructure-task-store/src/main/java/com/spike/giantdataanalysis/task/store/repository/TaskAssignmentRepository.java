package com.spike.giantdataanalysis.task.store.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spike.giantdataanalysis.task.store.domain.TaskAssignment;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {

  /**
   * 按工作者ID和是否被获取标志查询
   * @param workerId
   * @param taked
   * @return
   */
  @Query("SELECT t FROM TaskAssignment t WHERE t.workerId = ?1 AND t.taked = ?2")
  public List<TaskAssignment> queryByWorkerIdAndTaked(String workerId, boolean taked);

  /**
   * 按工作者ID查询未被获取的任务指派(关联任务执行表)
   * @param workerId
   * @return
   */
  @Query(value = "select ti.* " //
      + "from t_gda_task_assignment ta " //
      + "   inner join t_gda_task_info ti on ta.task_id = ti.id " //
      + "   left join t_gda_task_execution te on te.task_assignment_id = ta.id " //
      + "where ta.worker_id = ?1 and ta.taked = 0 and te.id is null", nativeQuery = true)
  public List<TaskAssignment> queryUnTakedTasks(String workerId);

  /**
   * 按任务定义ID和工作者ID更新是否获取字段
   * @param taskInfoId
   * @param workerId
   * @param taked
   */
  @Transactional
  @Modifying
  @Query("UPDATE TaskAssignment SET taked = ?3 WHERE taskId = ?1 AND workerId = ?2")
  public void updateTaked(long taskInfoId, String workerId, boolean taked);

}