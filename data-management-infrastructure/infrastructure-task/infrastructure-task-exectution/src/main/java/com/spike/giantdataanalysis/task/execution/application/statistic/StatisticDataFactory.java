package com.spike.giantdataanalysis.task.execution.application.statistic;

import java.util.List;

import com.google.common.collect.Lists;

public class StatisticDataFactory {
  private static final StatisticDataFactory INSTANCE = new StatisticDataFactory();

  private StatisticDataFactory() {
  }

  public static StatisticDataFactory I() {
    return INSTANCE;
  }

  public List<Long> getWorkloadIds() {
    return Lists.newArrayList(1l, 2l, 3l, 4l, 5l, 6l, 7l, 8l, 9l);
  }

}