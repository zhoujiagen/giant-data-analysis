package com.spike.giantdataanalysis.benchmark.example;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.benchmark.config.PGenParameter;
import com.spike.giantdataanalysis.benchmark.exception.BenchmarkException;
import com.spike.giantdataanalysis.benchmark.generator.AbstractPGen;
import com.spike.giantdataanalysis.benchmark.generator.PGenContext;
import com.spike.giantdataanalysis.benchmark.metric.PMRecord;

public final class ExampleWritePGen extends AbstractPGen {
  private static final Logger LOG = LoggerFactory.getLogger(ExampleWritePGen.class);

  public ExampleWritePGen(PGenParameter parameter, Set<PMRecord> PMRecordList) {
    super(parameter, PMRecordList);
  }

  @Override
  public void beforeGeneratePayload(PGenContext context) throws BenchmarkException {
  }

  @Override
  public void doGeneratePayload(PGenContext context) throws BenchmarkException {
    try {
      LOG.debug("开始执行生成负载");
      Thread.sleep(1000l);
      context.setSuccessCnt(100);
      context.setFailedCnt(1);
    } catch (InterruptedException e) {
      throw BenchmarkException.newException(e);
    }
  }

  @Override
  public void afterGeneratePayload(PGenContext context) throws BenchmarkException {
  }

}
