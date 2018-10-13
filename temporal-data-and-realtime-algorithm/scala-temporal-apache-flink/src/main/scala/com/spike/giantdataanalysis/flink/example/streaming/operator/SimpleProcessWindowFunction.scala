package com.spike.giantdataanalysis.flink.example.streaming.operator

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector


// IN, OUT, KEY, W <: Window
class SimpleProcessWindowFunction extends ProcessWindowFunction[(String, Int), String, String, TimeWindow] {
  override def process(key: String,
                       context: Context,
                       elements: Iterable[(String, Int)],
                       out: Collector[String]): Unit = {
    var count = 0
    for (e <- elements) {
      count += 1
    }

    //    context.globalState // cross window state
    //    context.windowState // per window state

    out.collect(s"Window(${context.window}) count: $count")
  }
}
