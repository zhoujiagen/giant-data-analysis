package com.spike.giantdataanalysis.benchmark.timing;

public interface Timingable {

  void work() throws TimingException;

  /** 指定出现异常是的原因 */
  long timeWhenException();
}
