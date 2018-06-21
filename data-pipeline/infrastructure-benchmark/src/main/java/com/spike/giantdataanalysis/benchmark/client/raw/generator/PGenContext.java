package com.spike.giantdataanalysis.benchmark.client.raw.generator;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 负载生成上下文
 */
public class PGenContext implements Serializable {
  private static final long serialVersionUID = 1L;

  private long startTime;
  private long endTime;
  private int successCnt; // 成功数量
  private int failedCnt; // 失败数量
  // 额外的上下文内容
  private Map<String, Object> contextMap = Maps.newHashMap();

  void reset() {
    successCnt = 0;
    failedCnt = 0;
    contextMap.clear();
  }

  void markStart() {
    this.startTime = new Date().getTime();
  }

  void markEnd() {
    this.endTime = new Date().getTime();
  }

  public void addContextValue(String key, Object value) {
    contextMap.put(key, value);
  }

  public Object getContextValue(String key) {
    return contextMap.get(key);
  }

  // getter/setter

  public long getStartTime() {
    return startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public int getSuccessCnt() {
    return successCnt;
  }

  public void setSuccessCnt(int successCnt) {
    this.successCnt = successCnt;
  }

  public int getFailedCnt() {
    return failedCnt;
  }

  public void setFailedCnt(int failedCnt) {
    this.failedCnt = failedCnt;
  }

}
