package com.spike.giantdataanalysis.benchmark.tools.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

/**
 * JMH示例.
 * 
 * <pre>
 * REF: http://tutorials.jenkov.com/java-performance/jmh.html
 * 
 * Run with userjar and <code>org.openjdk.jmh.Main</code>
 * </pre>
 * <p>
 * <b>WARNING</b>: JMH needs an uniquely named method, regardless of the arguments list.
 * @see package <code>org.openjdk.jmh.annotations</code>
 */
public class ExampleJMHHelloWorld {

  @State(Scope.Thread)
  public static class MyState {

    @Setup(Level.Trial)
    public void setUp() {
      sum = 0;
      System.out.print("Set up state  ");
    }

    @TearDown(Level.Trial)
    public void tearDown() {
      System.out.print("Tear down state  ");
    }

    public int a = 1;
    public int b = 2;
    public int sum;
  }

  // @Benchmark
  // public int testMethod() {
  // int a = 1;
  // int b = 2;
  // int sum = a + b;
  // // JVM constant folding, maybe as `return 3`
  // return sum; // return result due to dead code detection
  // }

  // with input parameter
  @Benchmark
  public int testMethodWithInput(MyState myState) {
    int sum = myState.a + myState.b;
    return sum; // fool JVM not to use constant folding
  }

  // with input parameter and output collector
  @Benchmark
  public void testMethodWithInputAndOutput(MyState myState, Blackhole blackhole) {
    int sum1 = myState.a + myState.b;
    int sum2 = sum1 + sum1;

    // return multiple results
    blackhole.consume(sum1);
    blackhole.consume(sum2);
  }

  // with output collector
  @Benchmark
  public void testMethodWithOutput(Blackhole blackhole) {
    int a = 1;
    int b = 2;
    int sum = a + b;
    blackhole.consume(sum); // use `Blackhole` due to dead code detection
  }

}
