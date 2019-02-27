package com.spike.giantdataanalysis.commons.jmx;

/**
 * @author zhoujiagen@gmail.com
 */
public interface ExampleMBean {

  long getStartTime();

  void setStartTime(long startTime);

  void sendNotify();
}
