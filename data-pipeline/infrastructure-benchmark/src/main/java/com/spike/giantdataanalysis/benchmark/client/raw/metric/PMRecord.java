package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import java.util.List;

/**
 * 性能度量记录
 * <p>
 * 因会被多线程共享访问, 一般实现为单例.
 */
public interface PMRecord {

  /** @return 描述 */
  String description();

  /** @return 是否支持添加{code PMBucket} */
  boolean supportAddPMBucket();

  void addSuccessPMBucket(PMRecordBucket metric);

  void addFailedPMBucket(PMRecordBucket metric);

  /** @return 性能度量值列表 */
  List<PMValue> values();

}
