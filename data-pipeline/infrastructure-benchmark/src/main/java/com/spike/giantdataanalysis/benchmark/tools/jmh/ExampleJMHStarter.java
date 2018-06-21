package com.spike.giantdataanalysis.benchmark.tools.jmh;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.openjdk.jmh.runner.options.WarmupMode;

/**
 * Example JMH Starter.
 * 
 * <pre>
 * Compile: $ mvn clean install
 * 
 * Default arguments: see {@link org.openjdk.jmh.runner.Defaults}
 * </pre>
 */
public class ExampleJMHStarter {
  public static void main(String[] args) {
    Options options = new OptionsBuilder()//

        // .include(".*com.spike.giantdataanalysis.benchmark.tools.jmh.ExampleJMHHelloWorld.*")//
        .include(ExampleJMHHelloWorld.class.getName())
        // .exclude("")//
        .mode(Mode.Throughput)//

        .timeUnit(TimeUnit.MINUTES)//
        .timeout(TimeValue.minutes(1L))//

        .forks(1)//
        .threads(1)//

        .warmupMode(WarmupMode.INDI)//
        .warmupIterations(1)//
        .warmupTime(TimeValue.seconds(10L))//

        .measurementIterations(1)//

        .jvmArgs("-Xms64m").jvmArgsAppend("-Xmx64m")//

        .verbosity(VerboseMode.EXTRA)//
        .build();

    try {
      Collection<RunResult> runResults = new Runner(options).run();
      for (RunResult rr : runResults) {
        System.out.println(rr);
      }
    } catch (RunnerException e) {
      e.printStackTrace();
    }
  }
}
