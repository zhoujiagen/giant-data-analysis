package com.spike.giantdataanalysis.benchmark.client.raw.gather;

import java.util.List;

import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMValue;

/**
 * 性能度量值收集器
 */
public interface PMValueGather {
  /** @return 性能度量值列表 */
  List<PMValue> gather();
}
