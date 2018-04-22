package com.spike.giantdataanalysis.model.algorithms.adt;

import org.junit.Test;

public class TestArrayStack {
  @Test
  public void example() {
    ArrayStack<Integer> q = new ArrayStack<>();

    q.push(1);
    q.push(2);
    q.push(3);
    q.push(4);
    q.push(5);
    q.push(6);
    q.push(7);
    q.push(8);
    q.push(9);

    int N = q.size();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < N; i++) {
      sb.append(q.pop() + " ");
    }
    System.out.println(sb.toString());

    // OUT
    // 5 4 3 2 1
  }
}
