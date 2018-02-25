package com.spike.giantdataanalysis.model.algorithms.adt;

import org.junit.Test;

public class TestBag {

  @Test
  public void statistic() {
    Bag<Double> numbers = new Bag<>();

    numbers.add(100d);
    numbers.add(99d);
    numbers.add(101d);
    numbers.add(120d);
    numbers.add(98d);
    numbers.add(107d);
    numbers.add(109d);
    numbers.add(81d);
    numbers.add(101d);
    numbers.add(90d);

    System.out.println(numbers);

    // 计算均值
    int N = numbers.size();
    double sum = 0.0;
    for (double x : numbers) {
      sum += x;
    }
    double mean = sum / N;

    // 计算标准差
    sum = 0.0;
    for (double x : numbers) {
      sum += (x - mean) * (x - mean);
    }
    double std = Math.sqrt(sum / (N - 1));

    System.out.println(mean);
    System.out.println(std);
  }
}
