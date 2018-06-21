package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import info.ganglia.gmetric4j.gmetric.GMetricType;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 关于数量的性能度量
 */
public class CountedPMRecord extends FullPMRecord {

  private static CountedPMRecord INSTANCE = new CountedPMRecord();

  private CountedPMRecord() {
  }

  public static CountedPMRecord getInstance() {
    return INSTANCE;
  }

  @Override
  public String description() {
    return "关于数量的性能度量";
  }

  @Override
  public List<PMValue> values() {
    List<PMValue> values = Lists.newArrayList();

    values.add(new PMValueBuilder().metric("totalCnt").value(String.valueOf(totalCnt()))
        .valueType(GMetricType.INT32).build());
    values.add(new PMValueBuilder().metric("successCnt").value(String.valueOf(successCnt()))
        .valueType(GMetricType.INT32).build());
    values.add(new PMValueBuilder().metric("failedCnt").value(String.valueOf(failedCnt()))
        .valueType(GMetricType.INT32).build());

    return values;
  }

}
