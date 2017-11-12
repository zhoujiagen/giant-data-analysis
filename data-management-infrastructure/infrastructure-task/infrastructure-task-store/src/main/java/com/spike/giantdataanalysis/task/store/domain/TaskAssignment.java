package com.spike.giantdataanalysis.task.store.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.spike.giantdataanalysis.task.store.annotation.Comment;

/**
 * 任务指派定义.
 * @author zhoujiagen
 */
@Comment("任务指派")
@Entity
@Table(name = "T_GDA_TASK_ASSIGNMENT")
public class TaskAssignment extends BaseModel {
  private static final long serialVersionUID = 1L;

  @Comment("任务定义")
  @ManyToOne
  @JoinColumn(name = "task_id")
  private TaskInfo taskInfo;

  @Column(name = "task_id", insertable = false, updatable = false)
  private long taskId;

  @Comment("工作者标识")
  @Column(name = "worker_id")
  private String workerId;

  public TaskInfo getTaskInfo() {
    return taskInfo;
  }

  public void setTaskInfo(TaskInfo taskInfo) {
    this.taskInfo = taskInfo;
  }

  public long getTaskId() {
    return taskId;
  }

  public void setTaskId(long taskId) {
    this.taskId = taskId;
  }

  public String getWorkerId() {
    return workerId;
  }

  public void setWorkerId(String workerId) {
    this.workerId = workerId;
  }
}
