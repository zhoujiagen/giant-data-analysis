package com.spike.giantdataanalysis.flink.example

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
//import org.apache.flink.api.common.typeinfo.TypeInformation
import Models.WordWithCount

object SocketWindowWordCount {
  def main(args : Array[String]) : Unit = {

    // the port to connect to
    val port : Int = 9000

    // get the execution environment
    val env : StreamExecutionEnvironment =
      StreamExecutionEnvironment.getExecutionEnvironment
    //      StreamExecutionEnvironment.createLocalEnvironment(1) // local test

    // get input data by connecting to the socket
    val text = env.socketTextStream("localhost", port, '\n')

    // parse the data, group it, window it, and aggregate the counts
    // TODO(zhoujiagen) why do we need this??? RTFSD!!!
    // FUCK YOU, dummy notor!!!, user `import org.apache.flink.streaming.api.scala._`
    //    implicit val typeInfo = TypeInformation.of(classOf[WordWithCount])
    //    implicit val typeInfoString = TypeInformation.of(classOf[String])

    val windowCounts = text.flatMap { w ⇒ w.split("\\s") }
      .map { w ⇒ WordWithCount(w, 1) }
      .keyBy("word")
      .timeWindow(Time.seconds(5), Time.seconds(1))
      .sum("count")

    // print the results with a single thread, rather than in parallel
    windowCounts.print().setParallelism(1)

    env.execute("Socket Window WordCount")
  }

}