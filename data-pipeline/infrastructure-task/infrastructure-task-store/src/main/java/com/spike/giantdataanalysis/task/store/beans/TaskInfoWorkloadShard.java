package com.spike.giantdataanalysis.task.store.beans;

import java.io.Serializable;

public class TaskInfoWorkloadShard implements Serializable {
  private static final long serialVersionUID = 1L;

  private String workloadShardKey;
  private long cnt;

  public TaskInfoWorkloadShard(String workloadShardKey, long cnt) {
    this.workloadShardKey = workloadShardKey;
    this.cnt = cnt;
  }

  public String getWorkloadShardKey() {
    return workloadShardKey;
  }

  public void setWorkloadShardKey(String workloadShardKey) {
    this.workloadShardKey = workloadShardKey;
  }

  public long getCnt() {
    return cnt;
  }

  public void setCnt(long cnt) {
    this.cnt = cnt;
  }

  @Override
  public String toString() {
    return "TaskInfoWorkloadShard [workloadShardKey=" + workloadShardKey + ", cnt=" + cnt + "]";
  }
}
