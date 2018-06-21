package com.spike.giantdataanalysis.benchmark.client.raw.generator;

import java.util.Date;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.benchmark.client.raw.config.PGenParameter;
import com.spike.giantdataanalysis.benchmark.client.raw.exception.BenchmarkException;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMRecord;
import com.spike.giantdataanalysis.benchmark.client.raw.metric.PMRecordBucket;

public abstract class AbstractPGen implements PGen {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractPGen.class);

  protected PGenParameter parameter;

  protected Set<PMRecord> PMRecords;

  public AbstractPGen(PGenParameter parameter, Set<PMRecord> PMRecords) {
    this.parameter = parameter;
    this.PMRecords = PMRecords;
  }

  @Override
  public void run() {
    while (true) {
      this.generate();
    }
  }

  @Override
  public void generate() throws BenchmarkException {

    LOG.debug("负载生成准备上下文");
    PGenContext context = new PGenContext();
    context.reset();

    LOG.debug("负载生成预处理");
    beforeGeneratePayload(context);

    context.markStart();
    LOG.debug("负载生成处理");
    doGeneratePayload(context);
    context.markEnd();

    LOG.debug("负载生成后处理");
    afterGeneratePayload(context);

    if (PMRecords != null && PMRecords.size() > 0) {
      LOG.debug("负载生成上报性能统计值");
      PMRecordBucket succesPMBucket =
          new PMRecordBucket(context.getStartTime(), context.getEndTime(), context.getSuccessCnt());
      PMRecordBucket failedPMBucket =
          new PMRecordBucket(context.getStartTime(), context.getEndTime(), context.getFailedCnt());
      for (PMRecord pmr : PMRecords) {
        if (pmr.supportAddPMBucket()) {
          pmr.addSuccessPMBucket(succesPMBucket);
          pmr.addFailedPMBucket(failedPMBucket);
        }
      }
    }

    try {
      long actionDuration = parameter.getActionDuration();

      long dftDuration = 10000l; // 10s
      if (actionDuration <= 0l) {
        // do nothing
      } else if (actionDuration > 0l && actionDuration <= dftDuration) {
        Thread.sleep(actionDuration);
      } else if (actionDuration > dftDuration) {
        // range delta: 10s
        long nextLong = new Random(new Date().getTime()).nextLong();
        nextLong = nextLong > 0 ? nextLong : -nextLong;
        Thread.sleep(actionDuration + (nextLong % 10000));
      }
    } catch (InterruptedException e) {
      LOG.error(AbstractPGen.class.getSimpleName() + " sleep failed", e);
      throw BenchmarkException.newException(e);
    }
  }

  /**
   * 生成负载前操作
   * @throws BenchmarkException
   */
  public abstract void beforeGeneratePayload(PGenContext context) throws BenchmarkException;

  /**
   * 生成负载
   * @throws BenchmarkException
   */
  public abstract void doGeneratePayload(PGenContext context) throws BenchmarkException;

  /**
   * 生成负载后操作
   * @throws BenchmarkException
   */
  public abstract void afterGeneratePayload(PGenContext context) throws BenchmarkException;
}
