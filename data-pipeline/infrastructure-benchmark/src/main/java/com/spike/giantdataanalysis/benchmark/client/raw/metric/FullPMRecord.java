package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 全记录的性能度量
 */
public abstract class FullPMRecord implements PMRecord {

  protected ConcurrentLinkedQueue<PMRecordBucket> successPMBuckets = new ConcurrentLinkedQueue<>();
  protected ConcurrentLinkedQueue<PMRecordBucket> failedPMBuckets = new ConcurrentLinkedQueue<>();
  /** 成功的总数量 */
  protected AtomicLong successCnt = new AtomicLong(0l);
  /** 失败的总数量 */
  protected AtomicLong failedCnt = new AtomicLong(0l);

  /** 开始时间 */
  private long startTime = new Date().getTime();

  public ConcurrentLinkedQueue<PMRecordBucket> getSuccessPMBuckets() {
    return successPMBuckets;
  }

  public ConcurrentLinkedQueue<PMRecordBucket> getFailedPMBuckets() {
    return failedPMBuckets;
  }

  @Override
  public String description() {
    return "全记录的性能度量";
  }

  @Override
  public boolean supportAddPMBucket() {
    return true;
  }

  @Override
  public void addSuccessPMBucket(PMRecordBucket bucket) {
    successPMBuckets.add(bucket);
    successCnt.addAndGet(bucket.getWorkUnitCnt());
  }

  @Override
  public void addFailedPMBucket(PMRecordBucket bucket) {
    failedPMBuckets.add(bucket);
    failedCnt.addAndGet(bucket.getWorkUnitCnt());
  }

  public long getGlobalDuration() {
    return new Date().getTime() - startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long successCnt() {
    return successCnt.longValue();
  }

  public long failedCnt() {
    return failedCnt.longValue();
  }

  public long totalCnt() {
    return successCnt() + failedCnt();
  }

}
