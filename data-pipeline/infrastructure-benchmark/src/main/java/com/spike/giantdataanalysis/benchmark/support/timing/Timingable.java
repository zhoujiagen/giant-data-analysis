package com.spike.giantdataanalysis.benchmark.support.timing;

public interface Timingable {

  void work() throws TimingException;

  /** 指定出现异常时的原因 */
  long timeWhenException();
}
