package com.spike.giantdataanalysis.task.execution.core.executor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.spike.giantdataanalysis.coordination.CoordinationRole;

/**
 * 任务执行者信息.
 * @author zhoujiagen
 */
public class TaskExecutorInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;
  private CoordinationRole role;
  private TaskExecutorDuty duty;
  private Map<String, List<String>> ips;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CoordinationRole getRole() {
    return role;
  }

  public void setRole(CoordinationRole role) {
    this.role = role;
  }

  public Map<String, List<String>> getIps() {
    return ips;
  }

  public void setIps(Map<String, List<String>> ips) {
    this.ips = ips;
  }

  public TaskExecutorDuty getDuty() {
    return duty;
  }

  public void setDuty(TaskExecutorDuty duty) {
    this.duty = duty;
  }

}
