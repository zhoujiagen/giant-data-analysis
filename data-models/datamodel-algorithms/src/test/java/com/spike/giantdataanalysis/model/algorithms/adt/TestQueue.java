package com.spike.giantdataanalysis.model.algorithms.adt;

import org.junit.Test;

public class TestQueue {

  @Test
  public void example() {
    Queue<Integer> q = new Queue<>();

    q.enqueue(1);
    q.enqueue(2);
    q.enqueue(3);
    q.enqueue(4);
    q.enqueue(5);

    int N = q.size();
    for (int i = 0; i < N; i++) {
      System.out.print(q.dequeue() + " ");
    }
    System.out.println();

    // OUT
    // 1 2 3 4 5
  }
}
