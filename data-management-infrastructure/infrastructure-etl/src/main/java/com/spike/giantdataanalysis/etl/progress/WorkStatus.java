package com.spike.giantdataanalysis.etl.progress;

public final class WorkStatus {
  private ProgressEnum progressEnum;
  private Long totalCount; // may be null
  private long handledCount;

  public ProgressEnum getProgressEnum() {
    return progressEnum;
  }

  public void setProgressEnum(ProgressEnum progressEnum) {
    this.progressEnum = progressEnum;
  }

  public Long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public long getHandledCount() {
    return handledCount;
  }

  public void setHandledCount(long handledCount) {
    this.handledCount = handledCount;
  }
}