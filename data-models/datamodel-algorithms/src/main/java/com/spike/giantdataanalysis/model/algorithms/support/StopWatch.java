package com.spike.giantdataanalysis.model.algorithms.support;

/**
 * 计时器.
 * @author zhoujiagen
 */
public class StopWatch {

  private final long start;

  public StopWatch() {
    start = System.currentTimeMillis();
  }

  /** 对象创建以来所经过的秒数. */
  public double elapsedSeconds() {
    long now = System.currentTimeMillis();
    return (now - start) / 1000.0;
  }
}
