package com.spike.giantdataanalysis.task.store.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
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
@Table(name = "T_GDA_TASK_ASSIGNMENT", //
    indexes = { @Index(name = "worker_id_idx", columnList = "worker_id") })
public class TaskAssignment extends BaseModel {
  private static final long serialVersionUID = 1L;

  @Comment("任务定义")
  @ManyToOne
  @JoinColumn(name = "task_id")
  private TaskInfo taskInfo;

  @Column(name = "task_id", insertable = false, updatable = false, nullable = false)
  private long taskId;

  @Comment("工作者标识")
  @Column(name = "worker_id", nullable = false)
  private String workerId;

  @Comment("是否被获取")
  @Column(name = "taked")
  private boolean taked = false;

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

  @Override
  public String toString() {
    return "TaskAssignment [taskInfo=" + taskInfo + ", taskId=" + taskId + ", workerId=" + workerId
        + "]";
  }

}
