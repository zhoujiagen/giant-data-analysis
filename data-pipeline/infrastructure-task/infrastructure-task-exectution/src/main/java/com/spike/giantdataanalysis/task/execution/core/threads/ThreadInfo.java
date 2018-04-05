package com.spike.giantdataanalysis.task.execution.core.threads;

import java.io.Serializable;

/** 线程信息 */
class ThreadInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  private String groupName;
  private String name;
  private Thread thread;

  public ThreadInfo(String groupName, String name, Thread thread) {
    this.groupName = groupName;
    this.name = name;
    this.thread = thread;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ThreadInfo other = (ThreadInfo) obj;
    if (groupName == null) {
      if (other.groupName != null) return false;
    } else if (!groupName.equals(other.groupName)) return false;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Thread getThread() {
    return thread;
  }

  public void setThread(Thread thread) {
    this.thread = thread;
  }

  @Override
  public String toString() {
    return "ThreadInfo [groupName=" + groupName + ", name=" + name + ", thread=" + thread + "]";
  }

}