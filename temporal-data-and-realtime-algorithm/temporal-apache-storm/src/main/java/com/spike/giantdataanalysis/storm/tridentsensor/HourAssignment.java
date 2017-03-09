package com.spike.giantdataanalysis.storm.tridentsensor;

import java.util.List;

import org.apache.storm.guava.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

import com.spike.giantdataanalysis.storm.tridentsensor.DiagnosisEventSpout.DiagnosisEvent;

/**
 * Trident Function: 添加{@link HourAssignment#FIELD_HOUR}, {@link HourAssignment#FIELD_GROUP_KEY}字段
 * @author zhoujiagen
 */
public class HourAssignment extends BaseFunction {
  private static final long serialVersionUID = 7212600117097527572L;
  private static final Logger LOG = LoggerFactory.getLogger(HourAssignment.class);

  public static final String FIELD_HOUR = "hour";
  /** 值的格式为city:diagnosisCode:hour */
  public static final String FIELD_GROUP_KEY = "cityDiseaseHour";

  @Override
  public void execute(TridentTuple tuple, TridentCollector collector) {
    DiagnosisEvent event = (DiagnosisEvent) tuple.getValue(0);
    // 获取 {@link CityAssignment}添加的字段
    String city = (String) tuple.getValue(1);

    long timestamp = event.getTime();
    long hourSinceEpoch = timestamp / 1000 / 60 / 60;
    String key = city + ":" + event.getDiagnosisCode() + ":" + hourSinceEpoch;
    LOG.debug("Key = {}", key);

    List<Object> values = Lists.newArrayList();
    values.add(hourSinceEpoch);
    values.add(key);
    collector.emit(values);
  }
}
