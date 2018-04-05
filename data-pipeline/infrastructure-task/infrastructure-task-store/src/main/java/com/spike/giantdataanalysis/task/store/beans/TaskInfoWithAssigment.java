package com.spike.giantdataanalysis.task.store.beans;

import java.io.Serializable;

public class TaskInfoWithAssigment implements Serializable {
  private static final long serialVersionUID = 1L;

  private long taskInfoId;
  private long taskAssignmentId;

  public TaskInfoWithAssigment(long taskInfoId, long taskAssignmentId) {
    this.taskInfoId = taskInfoId;
    this.taskAssignmentId = taskAssignmentId;
  }

  public long getTaskInfoId() {
    return taskInfoId;
  }

  public void setTaskInfoId(long taskInfoId) {
    this.taskInfoId = taskInfoId;
  }

  public long getTaskAssignmentId() {
    return taskAssignmentId;
  }

  public void setTaskAssignmentId(long taskAssignmentId) {
    this.taskAssignmentId = taskAssignmentId;
  }

  @Override
  public String toString() {
    return "TaskInfoWithAssigment [taskInfoId=" + taskInfoId + ", taskAssignmentId="
        + taskAssignmentId + "]";
  }

}
