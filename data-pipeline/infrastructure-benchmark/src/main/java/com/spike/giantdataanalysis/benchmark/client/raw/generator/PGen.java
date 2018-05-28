package com.spike.giantdataanalysis.benchmark.client.raw.generator;

import com.spike.giantdataanalysis.benchmark.client.raw.exception.BenchmarkException;

/** 负载生成器 */
public interface PGen extends Runnable {

  /**
   * 生成负载
   * @throws BenchmarkException
   */
  void generate() throws BenchmarkException;
}
