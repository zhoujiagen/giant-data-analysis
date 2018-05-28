package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import info.ganglia.gmetric4j.gmetric.GMetricType;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 关于操作时间的性能度量
 */
public class TimedPMRecord implements PMRecord {

  /** 最大操作时间 */
  private double maxTime = Double.MIN_VALUE;
  /** 最小操作时间 */
  private double minTime = Double.MAX_VALUE;
  /** 平均操作时间 */
  private double avgTime = 0d;

  private static TimedPMRecord INSTANCE = new TimedPMRecord();

  private TimedPMRecord() {
  }

  public static TimedPMRecord getInstance() {
    return INSTANCE;
  }

  /**
   * 副作用: 执行{@link #calculate()}
   * @param metrics
   */
  public TimedPMRecord(List<PMRecordBucket> buckets) {
    this.calculate(buckets);
  }

  @Override
  public String description() {
    return "关于操作时间的性能度量";
  }

  @Override
  public boolean supportAddPMBucket() {
    return false;
  }

  @Override
  public void addSuccessPMBucket(PMRecordBucket metric) {
  }

  @Override
  public void addFailedPMBucket(PMRecordBucket metric) {
  }

  private void calculate(List<PMRecordBucket> buckets) {
    if (buckets == null || buckets.size() == 0) {
      return;
    }

    double totalTime = 0d;
    double delta = 0d;
    for (PMRecordBucket metric : buckets) {
      delta = metric.duration();
      totalTime += delta;

      if (maxTime < delta) {
        maxTime = delta;
      }
      if (minTime > delta) {
        minTime = delta;
      }
    }
    this.avgTime = totalTime / buckets.size();
  }

  /** 默认为{@code Double.MIN_VALUE} */
  public double getMaxTime() {
    return maxTime;
  }

  /** 默认为{@code Double.MAX_VALUE} */
  public double getMinTime() {
    return minTime;
  }

  /** 默认为{@code 0} */
  public double getAvgTime() {
    return avgTime;
  }

  @Override
  public List<PMValue> values() {
    List<PMValue> values = Lists.newArrayList();

    values.add(new PMValueBuilder().metric("time.max").value(String.valueOf(getMaxTime()))
        .valueType(GMetricType.DOUBLE).build());
    values.add(new PMValueBuilder().metric("time.min").value(String.valueOf(getMinTime()))
        .valueType(GMetricType.DOUBLE).build());
    values.add(new PMValueBuilder().metric("time.avg").value(String.valueOf(getAvgTime()))
        .valueType(GMetricType.DOUBLE).build());

    return values;
  }

}
