package com.spike.giantdataanalysis.model.algorithms.support;

import org.junit.Test;

import com.spike.giantdataanalysis.model.algorithms.example.ThreeSum;

public class TestStopWatch {

  @Test
  public void timing() {
    for (int N = 250; N < Integer.MAX_VALUE; N += N) {
      this.experiment(N);
    }
  }

  // 问题规模为N的实验
  private void experiment(int N) {
    int MAX = 1000000;
    int[] a = new int[N];
    for (int i = 0; i < N; i++) {
      a[i] = Statistics.uniform(-MAX, MAX);
    }

    StopWatch sw = new StopWatch();
    int cnt = ThreeSum.basicSolution(a);
    System.out.printf("N=%7d, Time=%5.1f, found: %d\n", N, sw.elapsedSeconds(), cnt);
  }

}
