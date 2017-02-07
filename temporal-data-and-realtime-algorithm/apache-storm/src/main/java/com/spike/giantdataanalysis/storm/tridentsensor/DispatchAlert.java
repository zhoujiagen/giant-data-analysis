package com.spike.giantdataanalysis.storm.tridentsensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

/**
 * Trident Function: 接收到事件时退出应用
 * @author zhoujiagen
 */
public class DispatchAlert extends BaseFunction {
  private static final long serialVersionUID = 1058886051995517339L;
  private static final Logger LOG = LoggerFactory.getLogger(DispatchAlert.class);

  @Override
  public void execute(TridentTuple tuple, TridentCollector collector) {
    String alert = (String) tuple.getValue(0);

    LOG.warn("ALERT RECEIVED [" + alert + "]");
    LOG.warn("Dispatch the national guard!");

    System.exit(0);
  }

}
