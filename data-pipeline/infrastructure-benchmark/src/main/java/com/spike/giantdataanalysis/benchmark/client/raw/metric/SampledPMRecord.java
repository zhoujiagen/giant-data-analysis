package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import info.ganglia.gmetric4j.gmetric.GMetricType;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 关于采样的性能度量, 一般考虑操作的速率.
 */
public class SampledPMRecord extends FullPMRecord {

  /** 采样周期 */
  private long sampleDuration = DEFAULT_SAMPLE_DURATION;
  public static final long DEFAULT_SAMPLE_DURATION = 10000l; // 10s

  private static SampledPMRecord INSTANCE = new SampledPMRecord();

  private SampledPMRecord() {
  }

  /** @see #setSampleDuration(long) */
  public static SampledPMRecord getInstance() {
    return INSTANCE;
  }

  @Override
  public String description() {
    return "关于采样的性能度量, 一般考虑操作的速率.";
  }

  /**
   * 获取当前(最近的一个采样周期内)的操作速率, 粒度为每秒
   * @return
   */
  public double successRate() {

    long now = new Date().getTime(); // 当前时间

    long sumTotalWorkUnit = 0l;

    PMRecordBucket metricBin = null;
    long tempDurationStart;
    while (!successPMBuckets.isEmpty()) {
      metricBin = successPMBuckets.peek();
      tempDurationStart = metricBin.getDurationStart();
      // 出现度量时间在下一次采样开始时间(本次采样结束时间)之后的情况
      if (now - tempDurationStart < 0) {
        break;
      }
      // 出现度量时间在本次采样开始时间之前的情况
      if (now - tempDurationStart > sampleDuration) {
        metricBin = successPMBuckets.poll(); // take head to remove
        continue;
      }

      metricBin = successPMBuckets.poll(); // take head to calculate
      sumTotalWorkUnit += metricBin.getWorkUnitCnt();
    }

    return ((double) sumTotalWorkUnit) / sampleDuration * 1000;
  }

  /** @see #successRate() */
  public double failedRate() {

    long now = new Date().getTime(); // 当前时间

    long sumTotalWorkUnit = 0l;

    PMRecordBucket metricBin = null;
    long tempDurationStart;
    while (!failedPMBuckets.isEmpty()) {
      metricBin = failedPMBuckets.peek();
      tempDurationStart = metricBin.getDurationStart();
      // 出现度量时间在下一次采样开始时间(本次采样结束时间)之后的情况
      if (now - tempDurationStart < 0) {
        break;
      }
      // 出现度量时间在本次采样开始时间之前的情况
      if (now - tempDurationStart > sampleDuration) {
        metricBin = failedPMBuckets.poll(); // take head to remove
        continue;
      }

      metricBin = failedPMBuckets.poll(); // take head to calculate
      sumTotalWorkUnit += metricBin.getWorkUnitCnt();
    }

    return ((double) sumTotalWorkUnit) / sampleDuration * 1000;
  }

  @Override
  public List<PMValue> values() {
    List<PMValue> values = Lists.newArrayList();

    values.add(new PMValueBuilder().metric("success.rate").value(String.valueOf(successRate()))
        .valueType(GMetricType.DOUBLE).build());
    values.add(new PMValueBuilder().metric("failed.rate").value(String.valueOf(failedRate()))
        .valueType(GMetricType.DOUBLE).build());

    return values;
  }

  public void setSampleDuration(long sampleDuration) {
    if (sampleDuration <= 0) {
      sampleDuration = DEFAULT_SAMPLE_DURATION;
    }
    this.sampleDuration = sampleDuration;
  }

}
