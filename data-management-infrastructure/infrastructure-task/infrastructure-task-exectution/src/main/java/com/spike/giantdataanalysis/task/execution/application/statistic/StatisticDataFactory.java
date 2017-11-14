package com.spike.giantdataanalysis.task.execution.application.statistic;

import java.util.Set;

import com.google.common.collect.Sets;

public class StatisticDataFactory {
  private static final StatisticDataFactory INSTANCE = new StatisticDataFactory();

  private StatisticDataFactory() {
  }

  public static StatisticDataFactory I() {
    return INSTANCE;
  }

  public Set<String> getWorkloadIds(int size) {
    if (size <= 0) {
      size = 10;
    }
    Set<String> result = Sets.newHashSet();
    for (int i = 1; i <= size; i++) {
      result.add(String.valueOf(i));
    }

    return result;
  }

}
