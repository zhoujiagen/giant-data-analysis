package com.spike.giantdataanalysis.task.store.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.spike.giantdataanalysis.task.store.annotation.Comment;

/**
 * 任务定义实体.
 * <p>
 * 注意: 这里通过{@link #workloadShardKey}和{@link #workloadShardInfo}将Spring Batch中Job和JobInstance融合在一起.
 * @author zhoujiagen
 */
@Comment("任务定义")
@Entity
@Table(name = "T_GDA_TASK_INFO")
public class TaskInfo extends BaseModel {
  private static final long serialVersionUID = 1L;

  @Comment("负载分片键")
  @Column(name = "workload_shardkey", nullable = false)
  private String workloadShardKey;

  @Comment("负载分片信息")
  @Column(name = "workload_shardinfo", length = 1000, nullable = false)
  private String workloadShardInfo;

  @Comment("负载分片大小")
  @Column(name = "workload_size", nullable = true)
  private Long workloadSize;

  /** 任务类型 */
  public enum TaskType {
    REGUALR, REDO
  }

  @Comment("任务类型")
  @Enumerated(EnumType.STRING)
  @Column(name = "task_type")
  private TaskInfo.TaskType taskType = TaskInfo.TaskType.REGUALR;

  @Comment("任务是否已指派标志")
  @Column(name = "is_assigned")
  private boolean assigned = false;

  @Comment("任务状态")
  @Enumerated(EnumType.STRING)
  @Column(name = "task_status")
  private TaskExecution.TaskStatus taskStatus = TaskExecution.TaskStatus.READY;

  /**
   * @return 任务定义是否有效
   */
  public boolean isValid() {
    if (StringUtils.isBlank(workloadShardKey) || StringUtils.isBlank(workloadShardInfo)) {
      return false;
    }

    return true;
  }

  public String getWorkloadShardKey() {
    return workloadShardKey;
  }

  public void setWorkloadShardKey(String workloadShardKey) {
    this.workloadShardKey = workloadShardKey;
  }

  public String getWorkloadShardInfo() {
    return workloadShardInfo;
  }

  public void setWorkloadShardInfo(String workloadShardInfo) {
    this.workloadShardInfo = workloadShardInfo;
  }

  public Long getWorkloadSize() {
    return workloadSize;
  }

  public void setWorkloadSize(Long workloadSize) {
    this.workloadSize = workloadSize;
  }

  public TaskInfo.TaskType getTaskType() {
    return taskType;
  }

  public void setTaskType(TaskInfo.TaskType taskType) {
    this.taskType = taskType;
  }

  public TaskExecution.TaskStatus getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(TaskExecution.TaskStatus taskStatus) {
    this.taskStatus = taskStatus;
  }

  public boolean isAssigned() {
    return assigned;
  }

  public void setAssigned(boolean assigned) {
    this.assigned = assigned;
  }

  @Override
  public String toString() {
    return "TaskInfo [workloadShardKey=" + workloadShardKey + ", workloadShardInfo="
        + workloadShardInfo + ", workloadSize=" + workloadSize + ", taskType=" + taskType
        + ", assigned=" + assigned + ", taskStatus=" + taskStatus + "]";
  }

}
