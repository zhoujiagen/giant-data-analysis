package com.spike.giantdataanalysis.benchmark.client.raw.metric;

import java.util.Date;

/**
 * 性能度量记录的桶
 */
public class PMRecordBucket {
  /** 操作持续开始时间, 粒度: 毫秒 */
  private final long durationStart;
  /** 操作结束开始时间, 粒度: 毫秒 */
  private final long durationEnd;
  /** 操作单元总数 */
  private final long workUnitCnt;
  /** 记录生成的时间 */
  private final Date gmtRecordTime;;

  public PMRecordBucket(long durationStart, long durationEnd, long workUnitCnt) {
    this.durationStart = durationStart;
    this.durationEnd = durationEnd;
    this.workUnitCnt = workUnitCnt;
    gmtRecordTime = new Date();
  }

  public long getDurationStart() {
    return durationStart;
  }

  public long getDurationEnd() {
    return durationEnd;
  }

  public long getWorkUnitCnt() {
    return workUnitCnt;
  }

  public Date getGmtRecordTime() {
    return gmtRecordTime;
  }

  public long duration() {
    return durationEnd - durationStart;
  }

}
