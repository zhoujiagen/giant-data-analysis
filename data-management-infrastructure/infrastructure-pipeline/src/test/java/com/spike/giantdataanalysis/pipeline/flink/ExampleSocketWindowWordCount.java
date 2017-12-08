package com.spike.giantdataanalysis.pipeline.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

// REF: https://ci.apache.org/projects/flink/flink-docs-release-1.3/quickstart/setup_quickstart.html
public class ExampleSocketWindowWordCount {

  public static void main(String[] args) throws Exception {
    // 应用参数
    // final int port;
    // try {
    // final ParameterTool params = ParameterTool.fromArgs(args);
    // port = params.getInt("port");
    // } catch (Exception e) {
    // System.err.println("No port specified. Please run 'SocketWindowWordCount --port <port>'");
    // return;
    // }
    final int port = 9000;

    // 创建执行环境
    final StreamExecutionEnvironment see = StreamExecutionEnvironment.createLocalEnvironment();

    // Source: 从Socket获取数据
    DataStream<String> input = see.socketTextStream("localhost", port, "\n");

    // 计算: 解析, 分组, 窗口化, 聚合
    FlatMapFunction<String, WordWithCount> flatMapper =
        new FlatMapFunction<String, WordWithCount>() {
          private static final long serialVersionUID = 1L;

          @Override
          public void flatMap(String value, Collector<WordWithCount> out) throws Exception {
            for (String word : value.split("\\s")) {
              out.collect(new WordWithCount(word, 1));
            }
          }
        };
    ReduceFunction<WordWithCount> reduceFunction =
        new ReduceFunction<ExampleSocketWindowWordCount.WordWithCount>() {
          private static final long serialVersionUID = 1L;

          @Override
          public WordWithCount reduce(WordWithCount value1, WordWithCount value2) throws Exception {
            return new WordWithCount(value1.word, value1.count + value2.count);
          }
        };
    DataStream<WordWithCount> windowedCount = input//
        .flatMap(flatMapper)//
        .keyBy("word")//
        .timeWindow(Time.seconds(5l), Time.seconds(1l))//
        .reduce(reduceFunction);

    // Sink: 输出结果
    windowedCount.print().setParallelism(1);

    String jobName = ExampleSocketWindowWordCount.class.getSimpleName();
    see.execute(jobName);
  }

  public static class WordWithCount {

    public String word;
    public long count;

    public WordWithCount() {
    }

    public WordWithCount(String word, long count) {
      this.word = word;
      this.count = count;
    }

    @Override
    public String toString() {
      return word + " : " + count;
    }
  }
}
