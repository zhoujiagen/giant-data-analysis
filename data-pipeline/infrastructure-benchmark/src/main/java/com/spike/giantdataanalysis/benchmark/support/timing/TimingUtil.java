package com.spike.giantdataanalysis.benchmark.support.timing;

import com.google.common.base.Preconditions;

public class TimingUtil {

  public static class TimingSummaryBuilder {
    private TimingSummary summary;

    public TimingSummaryBuilder() {
      this.summary = new TimingSummary();
    }

    public TimingSummaryBuilder max(double maxTime) {
      this.summary.setMaxTime(maxTime);
      return this;
    }

    public TimingSummaryBuilder min(double minTime) {
      this.summary.setMinTime(minTime);
      return this;
    }

    public TimingSummaryBuilder avg(double avgTime) {
      this.summary.setAvgTime(avgTime);
      return this;
    }

    public TimingSummary build() {
      return this.summary;
    }

  }

  /**
   * 执行时间汇总, 单位: 毫秒.
   */
  public static class TimingSummary {
    private double maxTime = Double.MAX_VALUE;
    private double minTime = Double.MAX_VALUE;
    private double avgTime = Double.MAX_VALUE;

    public TimingSummary() {
    }

    public void setMaxTime(double maxTime) {
      this.maxTime = maxTime;
    }

    public void setMinTime(double minTime) {
      this.minTime = minTime;
    }

    public void setAvgTime(double avgTime) {
      this.avgTime = avgTime;
    }

    public double getMaxTime() {
      return maxTime;
    }

    public double getMinTime() {
      return minTime;
    }

    public double getAvgTime() {
      return avgTime;
    }

    @Override
    public String toString() {
      return "TimingSummary [maxTime=" + maxTime + ", minTime=" + minTime + ", avgTime=" + avgTime
          + "] ms";
    }
  }

  /**
   * 最大/最小/平均执行时间, 单位: 毫秒.
   * @param times
   * @param timeable
   * @return
   */
  public static TimingSummary timing(int times, Timingable timingable) {
    Preconditions.checkArgument(times > 0, "Argument times should be positive!");
    Preconditions.checkArgument(timingable != null, "Argument timingable should not be null!");

    double maxTime = Double.MIN_VALUE;
    double minTime = Double.MAX_VALUE;

    long totalTime = 0l;
    long _delta = 0l;
    for (int i = 0; i < times; i++) {
      long _start = System.currentTimeMillis();
      try {
        timingable.work();
        long _end = System.currentTimeMillis();
        _delta = (_end - _start);
        if (maxTime < _delta) {
          maxTime = _delta;
        }
        if (minTime > _delta) {
          minTime = _delta;
        }
      } catch (TimingException e) {
        _delta = timingable.timeWhenException();
        if (maxTime < _delta) {
          maxTime = _delta;
        }
      }

      totalTime += _delta;
    }
    double avgTime = ((double) totalTime) / times;

    return new TimingSummaryBuilder().avg(avgTime).min(minTime).max(maxTime).build();
  }
}
