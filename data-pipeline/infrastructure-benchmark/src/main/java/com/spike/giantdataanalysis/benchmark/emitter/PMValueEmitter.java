package com.spike.giantdataanalysis.benchmark.emitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.benchmark.exception.BenchmarkException;
import com.spike.giantdataanalysis.benchmark.metric.PMValue;

/**
 * 性能指标值提交器
 */
public interface PMValueEmitter extends AutoCloseable {

  void emit(PMValue pmValue) throws BenchmarkException;

  /** 默认的日志输出提交器 */
  static PMValueEmitter DEFAULT = new PMValueEmitter() {
    final Logger LOG = LoggerFactory.getLogger(PMValueEmitter.class);

    @Override
    public void emit(PMValue pmValue) {
      LOG.info("提交性能指标值: {}", pmValue.toString());
    }

    @Override
    public void close() throws Exception {
    }

  };

}
