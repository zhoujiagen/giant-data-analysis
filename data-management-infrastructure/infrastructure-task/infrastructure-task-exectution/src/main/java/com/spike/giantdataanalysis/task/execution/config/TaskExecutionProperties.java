package com.spike.giantdataanalysis.task.execution.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.spike.giantdataanalysis.coordination.CoordinationRole;

/**
 * 任务执行参数配置.
 * @author zhoujiagen
 */
@Primary
@Component
@ConfigurationProperties(prefix = "task")
public class TaskExecutionProperties {

  // ======================================== task.*
  // task.role
  private String role;

  // task.isSingletonInCluster
  private boolean singletonInCluster;

  // task.checkAlivePeriod
  private long checkAlivePeriod;

  // ======================================== task.creator.*
  private final Creator creator = new Creator();

  public class Creator {

    private long checkWorkPeriod;
    private long initStart;
    private long initEnd;

    public long getCheckWorkPeriod() {
      return checkWorkPeriod;
    }

    public void setCheckWorkPeriod(long checkWorkPeriod) {
      this.checkWorkPeriod = checkWorkPeriod;
    }

    public long getInitStart() {
      return initStart;
    }

    public void setInitStart(long initStart) {
      this.initStart = initStart;
    }

    public long getInitEnd() {
      return initEnd;
    }

    public void setInitEnd(long initEnd) {
      this.initEnd = initEnd;
    }
  }

  // ======================================== task.assignor.*
  private final Assignor assignor = new Assignor();

  public class Assignor {
    private long checkWorkPeriod;

    public long getCheckWorkPeriod() {
      return checkWorkPeriod;
    }

    public void setCheckWorkPeriod(long checkWorkPeriod) {
      this.checkWorkPeriod = checkWorkPeriod;
    }

  }

  // ======================================== task.executor.*
  private final Executor executor = new Executor();

  public class Executor {
    private long checkWorkPeriod;
    private int slots;

    public long getCheckWorkPeriod() {
      return checkWorkPeriod;
    }

    public void setCheckWorkPeriod(long checkWorkPeriod) {
      this.checkWorkPeriod = checkWorkPeriod;
    }

    public int getSlots() {
      return slots;
    }

    public void setSlots(int slots) {
      this.slots = slots;
    }

  }

  // ======================================== task.coordication.*
  private final Coordination coordination = new Coordination();

  /** 协同相关配置属性 */
  public class Coordination {
    private String zookeeperConnectionString;
    private String membershippath;
    private String leadershippath;

    public String getZookeeperConnectionString() {
      return zookeeperConnectionString;
    }

    public void setZookeeperConnectionString(String zookeeperConnectionString) {
      this.zookeeperConnectionString = zookeeperConnectionString;
    }

    public String getMembershippath() {
      return membershippath;
    }

    public void setMembershippath(String membershippath) {
      this.membershippath = membershippath;
    }

    public String getLeadershippath() {
      return leadershippath;
    }

    public void setLeadershippath(String leadershippath) {
      this.leadershippath = leadershippath;
    }

  }

  public CoordinationRole getCoordinationRole() {
    return CoordinationRole.convert(role);
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public long getCheckAlivePeriod() {
    return checkAlivePeriod;
  }

  public void setCheckAlivePeriod(long checkAlivePeriod) {
    this.checkAlivePeriod = checkAlivePeriod;
  }

  public boolean isSingletonInCluster() {
    return singletonInCluster;
  }

  public void setSingletonInCluster(boolean singletonInCluster) {
    this.singletonInCluster = singletonInCluster;
  }

  public Coordination getCoordination() {
    return coordination;
  }

  public Creator getCreator() {
    return creator;
  }

  public Assignor getAssignor() {
    return assignor;
  }

  public Executor getExecutor() {
    return executor;
  }

}
