package com.spike.giantdataanalysis.track.htrace;

import java.util.Map;

import org.apache.htrace.core.HTraceConfiguration;
import org.apache.htrace.core.SpanId;
import org.apache.htrace.core.Tracer;

import com.google.common.collect.Maps;

/**
 * @author zhoujiagen@gmail.com
 */
public class ExampleHTrace {

  public static void main(String[] args) {

    // Configuration
    final Map<String, String> configMap = Maps.newHashMap();
    // Sampler
    configMap.put(Tracer.SAMPLER_CLASSES_KEY,
      org.apache.htrace.core.AlwaysSampler.class.getCanonicalName());
    // Receiver
    configMap.put(Tracer.SPAN_RECEIVER_CLASSES_KEY,
      org.apache.htrace.core.StandardOutSpanReceiver.class.getCanonicalName());

    // Tracer
    Tracer tracer = new Tracer.Builder("simple tracer")//
        .conf(new HTraceConfiguration() {

          @Override
          public String get(String key, String defaultValue) {
            String result = configMap.get(key);
            return result == null ? defaultValue : result;
          }

          @Override
          public String get(String key) {
            return configMap.get(key);
          }
        })

        .build();

    // ExampleTracedProgram program = new ExampleTracedProgram(tracer);
    // System.out.println(program.simpleMethod("hello"));
    // System.out.println(program.methodWithCall("hello"));
    // System.out.println(program.methodWithMultiCall("hello", 10));
    // System.out.println(program.methodWithThread("hello", 10));

    // 模拟RPC
    ExampleTracedRPCProgram rpcProgram = new ExampleTracedRPCProgram(tracer);
    rpcProgram.mocking(new SpanId(1L, 1L));
    rpcProgram.shutdown();
  }

}
