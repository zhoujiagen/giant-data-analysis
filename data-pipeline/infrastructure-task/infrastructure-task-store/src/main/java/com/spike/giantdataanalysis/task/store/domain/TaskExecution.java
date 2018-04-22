package com.spike.giantdataanalysis.task.store.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.spike.giantdataanalysis.task.store.annotation.Comment;

/**
 * 任务执行记录实体定义
 * @author zhoujiagen
 */
@Comment("任务执行")
@Entity
@Table(name = "T_GDA_TASK_EXECUTION")
public class TaskExecution extends BaseModel {
  private static final long serialVersionUID = 1L;

  @Comment("任务指派")
  @ManyToOne
  @JoinColumn(name = "task_assignment_id")
  private TaskAssignment taskAssignment;

  @Column(name = "task_assignment_id", insertable = false, updatable = false)
  private long taskAssignmentId;

  @Comment("工作者标识")
  @Column(name = "worker_id")
  private String workerId;

  @Comment("负载分片进展")
  @Column(name = "workload_progress", nullable = true)
  private long workloadProgress = 0l;

  @Comment("开始时间")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "start_time")
  private Date startTime;

  @Comment("结束时间")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "end_time")
  private Date endTime;

  /** 任务状态 */
  public enum TaskStatus {
    READY, DOING, DONE, CORRUPT
  }

  @Comment("任务状态")
  @Enumerated(EnumType.STRING)
  @Column(name = "task_status")
  private TaskExecution.TaskStatus taskStatus;

  @Comment("任务状态信息")
  @Column(name = "task_status_info")
  private String taskStatusInfo;

  // ======================================== 临时字段
  @Transient
  private long taskInfoId;

  public TaskAssignment getTaskAssignment() {
    return taskAssignment;
  }

  public void setTaskAssignment(TaskAssignment taskAssignment) {
    this.taskAssignment = taskAssignment;
  }

  public long getTaskAssignmentId() {
    return taskAssignmentId;
  }

  public void setTaskAssignmentId(long taskAssignmentId) {
    this.taskAssignmentId = taskAssignmentId;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public TaskExecution.TaskStatus getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(TaskExecution.TaskStatus taskStatus) {
    this.taskStatus = taskStatus;
  }

  public String getTaskStatusInfo() {
    return taskStatusInfo;
  }

  public void setTaskStatusInfo(String taskStatusInfo) {
    this.taskStatusInfo = taskStatusInfo;
  }

  public String getWorkerId() {
    return workerId;
  }

  public void setWorkerId(String workerId) {
    this.workerId = workerId;
  }

  public long getWorkloadProgress() {
    return workloadProgress;
  }

  public void setWorkloadProgress(long workloadProgress) {
    this.workloadProgress = workloadProgress;
  }

  @Override
  public String toString() {
    return "TaskExecution [taskAssignment=" + taskAssignment + ", taskAssignmentId="
        + taskAssignmentId + ", workerId=" + workerId + ", workloadProgress=" + workloadProgress
        + ", startTime=" + startTime + ", endTime=" + endTime + ", taskStatus=" + taskStatus
        + ", taskStatusInfo=" + taskStatusInfo + "]";
  }

  public long getTaskInfoId() {
    return taskInfoId;
  }

  public void setTaskInfoId(long taskInfoId) {
    this.taskInfoId = taskInfoId;
  }

}
