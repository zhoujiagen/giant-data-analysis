package com.spike.giantdataanalysis.benchmark.client.raw.snapshot;

/**
 * 快照执行器的运行参数
 */
public class PMSnapshotParameter {

  private String metricGroup;
  private long duration;
  private long checkDutation;

  public String getMetricGroup() {
    return metricGroup;
  }

  public void setMetricGroup(String metricGroup) {
    this.metricGroup = metricGroup;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public long getCheckDutation() {
    return checkDutation;
  }

  public void setCheckDutation(long checkDutation) {
    this.checkDutation = checkDutation;
  }

}
