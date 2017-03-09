package com.spike.giantdataanalysis.storm.tridentsensor;

import java.util.List;

import org.apache.storm.guava.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

/**
 * Trident Function: 超出阈值时发布事件
 * @author zhoujiagen
 */
public class OutbreakDetector extends BaseFunction {
  private static final long serialVersionUID = 5328715873301134051L;
  private static final Logger LOG = LoggerFactory.getLogger(OutbreakDetector.class);

  public static final String FIELD_ALERT = "alert";
  public static final int THRESHOLD = 10000;// 阈值

  @Override
  public void execute(TridentTuple tuple, TridentCollector collector) {
    String key = (String) tuple.getValue(0);
    Long count = (Long) tuple.getValue(1);

    if (count > THRESHOLD) {
      LOG.warn("Reach Threshold!");

      List<Object> values = Lists.newArrayList();
      values.add("Outbreak detected for [" + key + "]!");
      collector.emit(values);
    }
  }

}
