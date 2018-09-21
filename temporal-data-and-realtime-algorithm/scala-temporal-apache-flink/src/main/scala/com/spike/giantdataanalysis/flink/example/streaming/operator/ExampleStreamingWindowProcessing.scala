package com.spike.giantdataanalysis.flink.example.streaming.operator

import com.spike.giantdataanalysis.flink.example.streaming.localStreamEnv
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners._
import org.apache.flink.streaming.api.windowing.time.Time


/**
  * Demonstration of window operators: window assigner and window function.
  */
object ExampleStreamingWindowProcessing {
  val env = localStreamEnv


  def main(args: Array[String]): Unit = {
    //    tumbling_sliding_window()
    //    session_window()
    global_window()
  }

  //---------------------------------------------------------------------------
  //
  //---------------------------------------------------------------------------


  //---------------------------------------------------------------------------
  // 窗口处理函数
  //---------------------------------------------------------------------------

  private def window_function(): Unit = {
    val windowStream = stream(env)
      .keyBy(_._1)
      .timeWindow(Time.seconds(2))

    windowStream
      // ReduceFunction
      //      .reduce {
      //      (t1, t2) => (t1._1, t1._2 + t2._2)
      //    }
      // AggregateFunction
      //.aggregate(new SimpleAverageAggregate)
      // ProcessWindowFunction
      // performance consideration: see ProcessWindowFunction with Incremental Aggregation
      .process(new SimpleProcessWindowFunction)
      .print()
  }


  //---------------------------------------------------------------------------
  // 窗口: 事件, 处理, 会话, 全局
  //---------------------------------------------------------------------------

  private def tumbling_sliding_window(): Unit = {

    val textStream = stream(env)
      .keyBy(0)
      // 翻滚窗口: 事件时间
      //.window(TumblingEventTimeWindows.of(Time.seconds(5), Time.seconds(1)))
      // 翻滚窗口: 处理时间
      //.window(TumblingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(1)))
      // 滑动窗口: 事件时间
      //.window(SlidingEventTimeWindows.of(Time.seconds(5), Time.seconds(1)))
      // 滑动窗口: 处理时间
      .window(SlidingProcessingTimeWindows.of(Time.seconds(5), Time.seconds(1)))

    textStream.sum(1).print()

    env.execute()
  }




  private def session_window(): Unit = {
    val textStream = stream(env)
      .keyBy(0)
      //.window(EventTimeSessionWindows.withGap(Time.minutes(10))) // 事件时间, 固定间隔
      // .window(EventTimeSessionWindows.withDynamicGap(new SessionWindowTimeGapExtractor[(String, Int)] {
      //      override def extract(element: (String, Int)): Long = ???
      //    }))
      //.window(ProcessingTimeSessionWindows.withGap(Time.minutes(10))) // 处理时间, 固定间隔
      .window(DynamicProcessingTimeSessionWindows.withDynamicGap( // 动态处理时间
      new SessionWindowTimeGapExtractor[(String, Int)] {
        // just a demonstration
        override def extract(element: (String, Int)): Long = ???
      }
    ))

    textStream.sum(0).print()

  }


  private def global_window(): Unit = {
    val textStream = stream(env)
      .keyBy(0)
      .window(GlobalWindows.create())

    textStream.sum(1).print()
  }
}
